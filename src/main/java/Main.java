import java.net.URLEncoder;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;

import com.lsnare.film.model.Film;
import com.lsnare.film.service.HTTPService;

import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import static spark.Spark.get;

import com.heroku.sdk.jdbc.DatabaseUrl;

public class Main {

  public static void main(String[] args) {
    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    //get("/hello", (req, res) -> "Hello World");

    /*get("/", (request, response) -> {
      Map<String, Object> attributes = new HashMap<>();
      attributes.put("message", "Hello World!");

      return new ModelAndView(attributes, "_index.ftl");
    }, new FreeMarkerEngine());*/

      get("/", (request, response) -> {
          Map<String, Object> attributes = new HashMap<>();
          attributes.put("message", "Hello World!");

          return new ModelAndView(attributes, "index.ftl");
      }, new FreeMarkerEngine());

      //Page for adding a film to the database
      get("/add", (request, response) -> {
          Map<String, Object> attributes = new HashMap<>();
          attributes.put("message", "Hello World!");
          return new ModelAndView(attributes, "addFilm.ftl");
      }, new FreeMarkerEngine());

      post("/add", (request, response) -> {
          Map<String, Object> attributes = new HashMap<>();
          String res = "";
          String filmTitle = request.queryParams("filmTitle");

          try {
              filmTitle = URLEncoder.encode(filmTitle, "UTF-8");
              res = HTTPService.postTest(filmTitle);
          } catch (Exception e) {
              attributes.put("message", e.getMessage());
          } finally {
              attributes.put("message", res);
          }
          return new ModelAndView(attributes, "addFilm.ftl");
      }, new FreeMarkerEngine());

      //Search for a film in the database
      get("/search", (request, response) -> {
          Map<String, Object> attributes = new HashMap<>();
          attributes.put("message", "Hello World!");

          return new ModelAndView(attributes, "searchFilm.ftl");
      }, new FreeMarkerEngine());

    post("/search", (request, response) -> {
        Map<String, Object> attributes = new HashMap<>();
        Film result = new Film();
        String filmTitle = request.queryParams("filmTitleSearch");
        System.out.println("param: " + filmTitle);
        try {
            System.out.println("In try block in main");
            filmTitle = filmTitle.replace('+', ' ');
            filmTitle = URLEncoder.encode(filmTitle, "UTF-8");
            System.out.println("In try block in main after decode: " + filmTitle);
            result = HTTPService.searchTest(filmTitle);
            System.out.println("Result: " + result.getTitle() + " " + result.getIdIMDB() + " " + result.getYear() + " " + result.getPlot());
            attributes.put("idIMDB", result.getIdIMDB());
            attributes.put("title", result.getTitle());
            attributes.put("plot", result.getPlot());
            Integer year = new Integer(result.getYear());
            System.out.println("Year: " + year);
            attributes.put("year", year);
            System.out.println("After attributes");
        } catch (Exception e) {
            System.out.println("Web error: " + e);
        } finally {
            //attributes.put("message", "good");
        }

        return new ModelAndView(attributes, "searchFilm.ftl");
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
