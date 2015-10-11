package com.lsnare.film.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Actor;
import com.lsnare.film.model.Director;
import com.lsnare.film.model.Film;
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
    static String myAPIFilmsURL = "http://www.myapifilms.com/imdb?format=JSON"
                                + "&lang=en-us&uniqueName=0";


    /*******************************/
    /**        Search Utils       **/
    /*******************************/

    public static Film[] searchMyAPIFilmsByTitle(String filmTitle){
        //Add title search-specific filters onto URL
        String url = myAPIFilmsURL + "&filter=M&limit=10&title=" + filmTitle;
        log.info("Search URL: " + url);
        Film[] films = new Film[0];
        try{
            String res = HTTPService.sendGet(url);
            log.info("JSON: " + res);
            Gson gson = new GsonBuilder().create();
            films = gson.fromJson(res, Film[].class);

        } catch(Exception e){
            log.error("Error searching MyAPIFilms by title: " + e.getMessage());
        }
        return films;
    }

    public static Film searchMyAPIFilmsByIMDBId(String IMDBId){
        //Add IMDB Id search-specific filters onto URL
        String url = myAPIFilmsURL + "&actors=S&idIMDB=" + IMDBId;
        log.info("Search URL: " + url);
        Film film = new Film();
        try{
            String res = HTTPService.sendGet(url);
            log.info("JSON returned: " + res);
            Gson gson = new GsonBuilder().create();
            //Film JSON come back as an array, but will only have one result
            film = gson.fromJson(res, Film.class);

        } catch(Exception e){
            log.error("Error searching MyAPIFilms by IMDB Id: " + e.getMessage());
        }
        return film;
    }

    public static List<Film> searchDatabaseForFilmsByTitle(String filmTitle){
        List<Film> films = new ArrayList<>();
        try{
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            films = filmDAO.selectFilmsByTitle(filmTitle);
            log.info("Found " + films.size() + " films when searching");
        } catch(Exception e){
            log.error("Error searching database by title: " + e);
        }
        return films;
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

    public static Map<String, Object> buildMyAPIFilmsSearchResults(Film[] films){
        Map<String, Object> attributes = new HashMap();
        String filmData = "";
        if (films.length > 0){
            filmData += "<fieldset>";
            //Create a list of radio inputs with values set to each film's IMDB Id
            for (Film film : films){
                filmData += "<input type=\"radio\" name=\"film\" value=\"" + film.getIdIMDB() + "\">"
                        + film.getTitle() + "&nbsp" + film.getYear() + "<br>";
            }
            filmData += "<input type=\"submit\" name=\"insertFilm\" value=\"Insert Film\" form=\"searchResultsTable\"/>";
            filmData += "</fieldset>";
        }
        attributes.put("filmData", filmData);
        return attributes;
    }

    public static Map<String, Object> buildDatabaseFilmSearchResults(List<Film> films){
        String filmData = "";
        Map<String, Object> attributes = new HashMap();

        attributes.put("searchResultsHeader", "<h3>Search Results</h3>");

        if(films.size() > 0) {

            String shortPlot = "";
            String longPlot = "";
            int count = 0;

            filmData += "<table border=1> <col width=\"80\"> <col width=\"100\"> <col width=\"50\"> <col width=\"500\"> <col width=\"250\">"
                    + "<tr>"
                    + "<th>IMDB ID</th> <th>Title</th> <th>Year</th> <th>Plot</th> <th>Actors</th> <th>Director</th> </tr>";

            for (Film film : films) {
                //Get the first full sentence for the short plot
                shortPlot = film.getPlot().split("\\.", 25)[0] + "...";
                longPlot = film.getPlot();

                //HTML TR id field
                String rowId = "row_" + count;
                filmData += "<tr id = \"" + rowId + "\"><td>" + film.getIdIMDB() + "</td>"
                        + "<td>" + film.getTitle() + "</td>"
                        + "<td>" + film.getYear() + "</td>"
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
                        for (Actor actor : film.getActors()){
                            filmData += actor.getActorName() + "<br>";
                        }
                        //Fill in director list
                        filmData += "</td><td>";
                        for (Director director : film.getDirectors()){
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
            actorData += "<tr><th>Film</th><th>Role</th></tr>";

            for(String nextFilm : rolesForActor.keySet()){
                actorData += "<tr><td>" + nextFilm + "</td><td>" + rolesForActor.get(nextFilm) + "</td></tr>";
            }

            actorData += "</table>";
        }

        attributes.put("actorData", actorData);

        return attributes;
    }

}
