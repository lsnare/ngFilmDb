package com.lsnare.film.worker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Movie;
import com.lsnare.film.service.HTTPService;
import com.lsnare.film.util.FilmUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by lucian.snare on 11/20/2015.
 */
public class FilmCrewBatchWorker {
    public static final int RUN_INTERVAL = 1000 * 60 * 30;
    static Log log = LogFactory.getLog(FilmCrewBatchWorker.class);


    public static void main(String[] args) {
        while(true){
            try {
                ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
                FilmDAO filmDAO = (FilmDAO) context.getBean("filmDAO");
                //check film records that need to be updated
                log.info("Processing film updates...");
                List<Movie> movies = filmDAO.selectDirtyFilms();
                log.info("Found " + movies.size() + " movies to update");

                if (!movies.isEmpty()) {
                    //update all dirty records
                    for (Movie movie : movies) {
                        String url = FilmUtils.myAPIFilmsURL;
                        url = url.replace("__ATTR_TO_SEARCH__", "idIMDB="+movie.getIdIMDB()) + "&actors=F";
                        String res = HTTPService.sendGet(url);
                        log.info("JSON returned: " + res);

                        Gson gson = new GsonBuilder().create();
                        //Movie JSON come back as an array, but will only have one result
                        movie = gson.fromJson(res, Movie.class);
                        filmDAO.insertActors(movie.getActors());
                        filmDAO.insertDirectors(movie.getDirectors());
                        filmDAO.markFilmAsClean(movie.idIMDB);
                    }

                    //sleep
                    log.info("Movie updates complete");
                }
                Thread.sleep(RUN_INTERVAL);
            } catch (Exception e) {
                log.error("Error when processing dirty movies: " + e.getMessage());
            }
        }
    }

}
