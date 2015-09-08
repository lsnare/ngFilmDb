package com.lsnare.film.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Film;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lucian on 9/2/15.
 */
public class HTTPService {

    public static String postTest(String filmTitle){
        try{
            String res = sendGet("http://www.myapifilms.com/imdb?title=" + filmTitle +"&format=JSON&lang=en-us&actors=S");
            Gson gson = new GsonBuilder().create();
            Film film = gson.fromJson(res.substring(1, res.length()-1), Film.class);
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            filmDAO.insert(film);

        } catch(Exception e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "<b>Film added to the database successfully!</b>";

    }

    public static Film searchTest(String filmTitle){
        Film film = new Film();
        try{
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("Spring-Module.xml");
            FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
            film = filmDAO.selectFilms(filmTitle);
        } catch(Exception e){
            System.out.println("HTTPService error: " + e);
        }
        return film;
    }

    private static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }


}
