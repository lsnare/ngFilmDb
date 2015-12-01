package com.lsnare.film.worker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.dao.impl.FilmDAOImplementation;
import com.lsnare.film.model.Film;
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
                List<Film> films = filmDAO.selectDirtyFilms();
                log.info("Found " + films.size() + " films to update");

                //update all dirty records
                for(Film film : films){
                    String url = FilmUtils.myAPIFilmsURL + "&actors=F&idIMDB=" + film.idIMDB;
                    String res = HTTPService.sendGet(url);
                    log.info("JSON returned: " + res);

                    Gson gson = new GsonBuilder().create();
                    //Film JSON come back as an array, but will only have one result
                    film = gson.fromJson(res, Film.class);
                    filmDAO.insertActors(film.getActors());
                    filmDAO.insertDirectors(film.getDirectors());
                    filmDAO.markFilmAsClean(film.idIMDB);
                }

                //sleep
                log.info("Film updates complete");
                Thread.sleep(RUN_INTERVAL);
            } catch (Exception e) {

            }
        }
    }

}
