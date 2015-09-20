import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;

import static spark.Spark.*;

import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Film;
import com.lsnare.film.util.FilmUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;

import static spark.Spark.get;

import com.heroku.sdk.jdbc.DatabaseUrl;

public class Main {

    static Log log = LogFactory.getLog(Main.class);

    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        //Page for adding a film to the database
        get("/add", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        //Endpoint for searching MyAPIFilms
        get("/searchMyAPIFilms", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Film[] results = new Film[0];
            String filmTitle = request.queryParams("filmTitle");
            try {
                filmTitle = URLEncoder.encode(filmTitle, "UTF-8");
                results = FilmUtils.searchMyAPIFilmsByTitle(filmTitle);
                attributes = FilmUtils.buildMyAPIFilmsSearchResults(results);
            } catch (Exception e) {
                attributes.put("message", "Error: " + e.getMessage());
            }
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        //Endpoint for inserting a film into the database
        post("/insertFilm", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String id = request.queryParams("film");
            Film film = new Film();
            try {
                film = FilmUtils.searchMyAPIFilmsByIMDBId(id);
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Module.xml");
                FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
                filmDAO.insert(film);
            } catch (Exception e) {
                attributes.put("message", "Error on insert: " + e.getMessage());
            } finally {
                attributes.put("message", film.getTitle() + " was inserted into the database successfully!");
            }
            return new ModelAndView(attributes, "addFilm.ftl");
        }, new FreeMarkerEngine());

        get("/search", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "searchFilm.ftl");
        }, new FreeMarkerEngine());

        //Search for a film in the database
        post("/search", (request, response) -> {
            List<Film> results = new ArrayList<>();
            Map<String, Object> attributes = new HashMap();
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
            return new ModelAndView(attributes, "searchActor.ftl");
        }, new FreeMarkerEngine());

        //Search for a film in the database
        post("/searchActor", (request, response) -> {
            Map<String, String> results = new HashMap();
            Map<String, Object> attributes = new HashMap();
            String actorName = request.queryParams("actorNameSearch");
            try {
                actorName = URLDecoder.decode(actorName, "UTF-8");
                results = FilmUtils.searchDatabaseForActorRoles(actorName);
                attributes = FilmUtils.buildDatabaseActorRolesSearchResults(actorName, results);
                log.info("Adding " + results.size() + " results to the page");
            } catch (Exception e) {
                System.out.println("Error on search: " + e);
                attributes.put("error", e);
            }
            return new ModelAndView(attributes, "searchActor.ftl");
        }, new FreeMarkerEngine());

        get("/db", (req, res) -> {
            Connection connection = null;
            Map<String, Object> attributes = new HashMap<>();
            try {
                connection = DatabaseUrl.extract().getConnection();

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
                stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
                ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

                ArrayList<String> output = new ArrayList<String>();
                while (rs.next()) {
                    output.add("Read from DB: " + rs.getTimestamp("tick"));
                }

                attributes.put("results", output);
                return new ModelAndView(attributes, "db.ftl");
            } catch (Exception e) {
                attributes.put("message", "There was an error: " + e);
                return new ModelAndView(attributes, "error.ftl");
            } finally {
                if (connection != null) try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }, new FreeMarkerEngine());

    }

}
