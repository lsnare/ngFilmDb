package com.lsnare.film.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.exception.MyAPIFilmsConnectionException;
import com.lsnare.film.model.Actor;
import com.lsnare.film.model.Director;
import com.lsnare.film.model.Movie;
import com.lsnare.film.service.HTTPService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucian on 9/8/15.
 */
public class FilmUtils {

    static Log log = LogFactory.getLog(FilmUtils.class);
    public static String myAPIFilmsURL = "http://www.myapifilms.com/imdb/idIMDB?__ATTR_TO_SEARCH__"
                                        + "&format=json"
                                        + "&token=" + System.getenv("MY_API_FILMS_TOKEN")
                                        + "&lang=en-us&uniqueName=0";


    /*******************************/
    /**        Search Utils       **/
    /*******************************/

    private class Result {
        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        Data data;
    }

    private class Data{
        public Movie[] getMovies() {
            return movies;
        }

        public void setMovies(Movie[] movies) {
            this.movies = movies;
        }

        Movie[] movies;
    }

    public static Movie[] searchMyAPIFilmsByTitle(String filmTitle) throws MyAPIFilmsConnectionException{
        //Add title search-specific filters onto URL

        //filter=3 specifies movies only
        String url = myAPIFilmsURL + "&filter=3&limit=10";
        url = url.replace("__ATTR_TO_SEARCH__", "title="+filmTitle);
        log.info("Search URL: " + url);
        Movie[] movies = new Movie[0];
        try{
            String res = HTTPService.sendGet(url);
            log.info("JSON: " + res);
            Gson gson = new GsonBuilder().create();
            Result result = gson.fromJson(res, Result.class);
            movies = result.getData().getMovies();
            log.info("Found movies from data result: " + movies);
        } catch(Exception e){
            log.error("Error searching MyAPIFilms by title: " + e.getMessage());
            if (e.getMessage().contains("www.myapifilms.com")){
                throw new MyAPIFilmsConnectionException(e.getMessage());
            }
        }
        return movies;
    }

    public static Movie searchMyAPIFilmsByIMDBId(String IMDBId){
        //Add IMDB Id search-specific filters onto URL

        //actors=1 specifies simple list of actors
        String url = myAPIFilmsURL + "&actors=1";
        url = url.replace("__ATTR_TO_SEARCH__", "idIMDB="+IMDBId);
        log.info("Search URL: " + url);
        Movie movie = new Movie();
        try{
            String res = HTTPService.sendGet(url);
            log.info("JSON returned: " + res);
            Gson gson = new GsonBuilder().create();
            //Movie JSON come back as an array, but will only have one result
            Result result = gson.fromJson(res, Result.class);
            movie = result.getData().getMovies()[0];
            log.info("Found this movies from data result: " + movie);
        } catch(Exception e){
            log.error("Error searching MyAPIFilms by IMDB Id: " + e.getMessage());
        }
        return movie;
    }

    public static List<Movie> searchDatabaseForFilmsByTitle(String filmTitle){
        List<Movie> movies = new ArrayList<>();
        try{
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            movies = filmDAO.selectFilmsByTitle(filmTitle);
            log.info("Found " + movies.size() + " movies when searching");
        } catch(Exception e){
            log.error("Error searching database by title: " + e);
        }
        return movies;
    }

    public static Map<String, Map<String,String>> searchDatabaseForActorRoles(String actorName){
        Map<String, Map<String, String>> rolesForActor = new HashMap();
        try{
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            rolesForActor = filmDAO.selectRolesForActor(actorName);
            log.info("Found " + rolesForActor.size() + " roles when searching");
        } catch(Exception e){
            log.error("Error searching atabase for roles: " + e);
        }
        return rolesForActor;
    }

    /*******************************/
    /**       Page Builders       **/
    /*******************************/

    public static Map<String, Object> buildMyAPIFilmsSearchResults(Movie[] movies){
        Map<String, Object> attributes = new HashMap();
        String filmData = "";
        if (movies.length > 0){
            filmData += "<fieldset>";
            //Create a list of radio inputs with values set to each film's IMDB Id
            for (Movie movie : movies){
                filmData += "<input type=\"radio\" name=\"film\" value=\"" + movie.getIdIMDB() + "\">"
                        + "<i>" + movie.getTitle() + "</i>";
                if(movie.getYear() != null && !movie.getYear().isEmpty()){
                    filmData += " (" + movie.getYear() + ")";
                }
                if (movie.getDirectors().size() > 0){
                    filmData += ", directed by " + movie.getDirectors().get(0).getName();
                }
                filmData += "<br>";
            }
            filmData += "<input type=\"submit\" name=\"insertFilm\" value=\"Insert Film\" form=\"searchResultsTable\"/>";
            filmData += "</fieldset>";
        }
        attributes.put("filmData", filmData);
        return attributes;
    }

    public static Map<String, Object> buildDatabaseFilmSearchResults(List<Movie> movies){
        String filmData = "";
        Map<String, Object> attributes = new HashMap();

        attributes.put("searchResultsHeader", "<h3>Search Results</h3>");

        if(movies.size() > 0) {

            String shortPlot = "";
            String longPlot = "";
            int count = 0;

            filmData += "<table border=1> <col width=\"80\"> <col width=\"100\"> <col width=\"50\"> <col width=\"500\"> <col width=\"250\">"
                    + "<tr>"
                    + "<th>IMDB ID</th> <th>Title</th> <th>Year</th> <th>Plot</th> <th>Actors</th> <th>Director</th> </tr>";

            for (Movie movie : movies) {
                //Get the first full sentence for the short plot
                shortPlot = movie.getPlot().split("\\.", 25)[0] + "...";
                longPlot = movie.getPlot();

                //HTML TR id field
                String rowId = "row_" + count;
                filmData += "<tr id = \"" + rowId + "\"><td>" + movie.getIdIMDB() + "</td>"
                        + "<td>" + movie.getTitle() + "</td>"
                        + "<td>" + movie.getYear() + "</td>"
                        //Short plot
                        + "<td>" + shortPlot
                            + "<a href =\"#\" onclick=\"showLongPlot(\'" + rowId + "\')\"> More </a>"
                        + "</td>"
                        //Create hidden td tag to hold the longer plot description
                        + "<td style=\"display: none;\">" + longPlot
                            + "<a href=\"#\" onclick=\"showShortPlot(\'" + rowId + "\')\"> Less </a>"
                        + "</td>"
                        //Fill in actor list
                        + "<td>";
                        for (Actor actor : movie.getActors()){
                            filmData += actor.getActorName() + "<br>";
                        }
                        //Fill in director list
                        filmData += "</td><td>";
                        for (Director director : movie.getDirectors()){
                            filmData += director.getName() + "<br>";
                        }
                        filmData += "</td></tr>";

                count ++;
            }
            filmData += "</table>";
        } else {
            filmData = "<b>No results found!</b>";
        }
        attributes.put("filmData", filmData);

        return attributes;
    }

    public static Map<String, Object> buildDatabaseActorRolesSearchResults(Map<String, Map<String,String>> roles){
        String actorData = "";
        Map<String, Object> attributes = new HashMap();

        attributes.put("searchResultsHeader", "<h3>Search Results</h3>");

        for (String actorName : roles.keySet()){
            Map<String, String> rolesForActor = roles.get(actorName);

            actorData += "<h2>" + actorName + "</h2>";
            actorData += "<table border=1> <col width=\"150\"> <col width=\"150\">";
            actorData += "<tr><th>Movie</th><th>Role</th></tr>";

            for(String nextFilm : rolesForActor.keySet()){
                actorData += "<tr><td>" + nextFilm + "</td><td>" + rolesForActor.get(nextFilm) + "</td></tr>";
            }

            actorData += "</table>";
        }

        attributes.put("actorData", actorData);

        return attributes;
    }

}
