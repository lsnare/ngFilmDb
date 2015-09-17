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
                                + "&aka=0&business=0&seasons=0&seasonYear=0&technical=0&filter=M&"
                                + "exactFilter=0&limit=10&forceYear=0&lang=en-us&actors=N&biography=0&"
                                + "trailer=0&uniqueName=0&filmography=0&bornDied=0&starSign=0&"
                                + "actorActress=0&actorTrivia=0&movieTrivia=0&awards=0&moviePhotos=N&"
                                + "movieVideos=N&similarMovies=0&adultSearch=0&title=";


    /*******************************/
    /**        Search Utils       **/
    /*******************************/

    public static Film[] searchMyAPIFilmsByTitle(String filmTitle){
        String url = myAPIFilmsURL + filmTitle;
        log.info("URL: " + url);
        Film[] films = new Film[0];
        try{
            String res = HTTPService.sendGet(url);
            log.info("JSON: " + res);
            Gson gson = new GsonBuilder().create();
            films = gson.fromJson(res, Film[].class);

        } catch(Exception e){
            log.error(e.getMessage());
            //return e.getMessage();
        }
        return films;
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
            System.out.println("HTTPService error: " + e);
        }
        return films;
    }

    public static Map<String, String> searchDatabaseForActorRoles(String actorName){
        Map<String, String> rolesForActor = new HashMap();
        try{
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            rolesForActor = filmDAO.selectRolesForActor(actorName);
            log.info("Found " + rolesForActor.size() + " roles when searching");
        } catch(Exception e){
            System.out.println("HTTPService error: " + e);
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
            filmData += "<table border=1> <col width=\"100\"> <col width=\"50\"> <col width=\"500\">"
                    + "<tr><th>Title</th><th>Year</th><th>Plot</th></tr>";
            for (Film film : films){
                filmData += "<tr><td>"+film.getTitle()+"</td><td>"+film.getYear()+"</td><td>"+film.getPlot()+"</td></tr>";
            }
            filmData += "</table>";
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
            String tdShort = "";
            String tdLong="";
            int count = 0;

            filmData += "<table border=1> <col width=\"80\"> <col width=\"100\"> <col width=\"50\"> <col width=\"500\"> <col width=\"250\">"
                    + "<tr>"
                    + "<th>IMDB ID</th> <th>Title</th> <th>Year</th> <th>Plot</th> <th>Actors</th> <th>Director</th> </tr>";

            for (Film film : films) {
                //Get the first full sentence for the short plot
                shortPlot = film.getPlot().split("\\.", 25)[0] + "...";
                longPlot = film.getPlot();

                String rowId = "row_" + count;
                filmData += "<tr id = \"" + rowId + "\"><td>" + film.getIdIMDB() + "</td>"
                        + "<td>" + film.getTitle() + "</td>"
                        + "<td>" + film.getYear() + "</td>"
                        //Create hidden td tag to hold the longer plot description
                        + "<td>" + shortPlot
                            + "<a href =\"#\" onclick=\"showLongPlot(\'" + rowId + "\')\"> More </a>"
                        + "</td>"
                        + "<td style=\"display: none;\">" + longPlot
                            + "<a href=\"#\" onclick=\"showShortPlot(\'" + rowId + "\')\"> Less </a>"
                        + "</td>"
                        + "<td>";
                        for (Actor actor : film.getActors()){
                            filmData += actor.getActorName() + "<br>";
                        }
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

    public static Map<String, Object> buildDatabaseActorRolesSearchResults(String actorName, Map<String, String> roles){
        String actorData = "";
        Map<String, Object> attributes = new HashMap();

        attributes.put("searchResultsHeader", "<h3>Search Results</h3>");
        actorData += "<ul><li>" + actorName + "</li>";
        for (String role : roles.keySet()){
            actorData += "<ul><li>" + role + "&nbsp <i>" + roles.get(role) + "</i></li>";
        }
        actorData += "</ul>";
        attributes.put("actorData", actorData);

        return attributes;
    }

}
