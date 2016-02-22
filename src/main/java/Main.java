import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.*;

import static spark.Spark.*;

import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.exception.DuplicateFilmException;
import com.lsnare.film.exception.MyAPIFilmsConnectionException;
import com.lsnare.film.model.Movie;
import com.lsnare.film.service.HTTPService;
import com.lsnare.film.util.FilmUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;

import static spark.Spark.get;

public class Main {

    static Log log = LogFactory.getLog(Main.class);
    static boolean loggedIn = false;

    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "login.ftl");
        }, new FreeMarkerEngine());

        post("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            try {
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Module.xml");
                FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
                boolean isValidUser = filmDAO.login(username, password);
                if (isValidUser){
                    request.session(true);
                    request.session().attribute("sessionId", UUID.randomUUID().toString());
                    log.info("Created session: " + request.session().attribute("sessionId"));
                    loggedIn = true;
                } else {
                    attributes.put("error", "Could not login as this user");
                    return new ModelAndView(attributes, "login.ftl");
                }
            } catch(Exception e){
                attributes.put("error", "Error logging in: " + e.getMessage());
            }
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/logout", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            request.session().invalidate();
            loggedIn = false;
            response.redirect("/login");
            return new ModelAndView(attributes, "login.ftl");
        }, new FreeMarkerEngine());

        //Page for adding a film to the database
        get("/add", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        //Endpoint for searching MyAPIFilms
        get("/searchMyAPIFilms", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            Movie[] results = new Movie[0];
            String filmTitle = request.queryParams("filmTitle");
            try {
                filmTitle = URLEncoder.encode(filmTitle, "UTF-8");
                results = FilmUtils.searchMyAPIFilmsByTitle(filmTitle);
                attributes = FilmUtils.buildMyAPIFilmsSearchResults(results);
            } catch (MyAPIFilmsConnectionException e){
                attributes.put("error", e.getMessage());
            } catch(Exception e){
                attributes.put("error", "Error: " + e.getMessage());
            }
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        before("/insertFilm", (request, response) -> {
            boolean authenticated = request.session().attribute("sessionId") != null;
            log.info("Session: " + request.session().attribute("sessionId"));
            if (!authenticated){
                halt(401, "<html><body bgcolor=\"#7BA05B\"><h1>You do not have permission to perform this action. Please authenticate to continue.</h1></body></html>");
            }
        });

        //Endpoint for inserting a film into the database
        post("/insertFilm", (request, response) -> {
            log.info("Session: " + request.session().attribute("sessionId"));
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            String id = request.queryParams("film");
            Movie movie = new Movie();
            try {
                movie = FilmUtils.searchMyAPIFilmsByIMDBId(id);
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Module.xml");
                FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
                filmDAO.insert(movie);
                attributes.put("message", movie.getTitle() + " was inserted into the database successfully!");
            } catch (DuplicateFilmException e) {
                log.error(e);
                attributes.put("error", "Error on insert: " + e.getMessage());
            } catch (Exception e) {
                attributes.put("error", "Error on insert: " + e.getMessage());
            }
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        get("/search", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            return new ModelAndView(attributes, "searchFilm.ftl");
        }, new FreeMarkerEngine());

        //Search for a film in the database
        post("/search", (request, response) -> {
            List<Movie> results = new ArrayList<>();
            Map<String, Object> attributes = new HashMap();
            attributes.put("loggedIn", loggedIn);
            String filmTitle = request.queryParams("filmTitleSearch");
            try {
                filmTitle = URLDecoder.decode(filmTitle, "UTF-8");
                results = FilmUtils.searchDatabaseForFilmsByTitle(filmTitle);
                attributes = FilmUtils.buildDatabaseFilmSearchResults(results);
                log.info("Adding " + results.size() + " results to the page");
            } catch (Exception e) {
                System.out.println("Error on search: " + e);
                attributes.put("error", e);
            }
            return new ModelAndView(attributes, "searchFilm.ftl");
        }, new FreeMarkerEngine());

        get("/searchActor", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            return new ModelAndView(attributes, "searchActor.ftl");
        }, new FreeMarkerEngine());

        //Search for a film in the database
        post("/searchActor", (request, response) -> {
            Map<String, Map<String, String>> results = new HashMap();
            Map<String, Object> attributes = new HashMap();
            attributes.put("loggedIn", loggedIn);
            String actorName = request.queryParams("actorNameSearch");
            try {
                actorName = URLDecoder.decode(actorName, "UTF-8");
                results = FilmUtils.searchDatabaseForActorRoles(actorName);
                attributes = FilmUtils.buildDatabaseActorRolesSearchResults(results);
                log.info("Adding " + results.size() + " results to the page");
            } catch (Exception e) {
                System.out.println("Error on search: " + e);
                attributes.put("error", e);
            }
            return new ModelAndView(attributes, "searchActor.ftl");
        }, new FreeMarkerEngine());

        get("/searchDirector", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("loggedIn", loggedIn);
            return new ModelAndView(attributes, "searchDirector.ftl");
        }, new FreeMarkerEngine());

        //Search for a director in the database
        post("/searchDirector", (request, response) -> {
            Map<String, Map<String, String>> results = new HashMap();
            Map<String, Object> attributes = new HashMap();
            attributes.put("loggedIn", loggedIn);
            String directorName = request.queryParams("directorNameSearch");
            try {
                directorName = URLDecoder.decode(directorName, "UTF-8");
                results = FilmUtils.searchDatabaseForDirectors(directorName);
                attributes = FilmUtils.buildDirectorFilmSearchResults(results); //TODO
                log.info("Adding " + results.size() + " results to the page");
            } catch (Exception e) {
                System.out.println("Error on search: " + e);
                attributes.put("error", e);
            }
            return new ModelAndView(attributes, "searchDirector.ftl");
        }, new FreeMarkerEngine());
    }

}
