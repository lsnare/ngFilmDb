package com.lsnare.film.dao;
import com.lsnare.film.exception.DuplicateFilmException;
import com.lsnare.film.model.Actor;
import com.lsnare.film.model.Director;
import com.lsnare.film.model.Movie;

import java.util.List;
import java.util.Map;

/**
 * Created by lucian on 9/3/15.
 */
public interface FilmDAO {
    public void insert(Movie movie) throws DuplicateFilmException;
    public List<Movie> selectFilmsByTitle(String title);
    public Map<String, Map<String, String>> selectRolesForActor(String actorName);
    public Map<String, Map<String,String>> selectFilmsForDirector(String directorName);
    public List<Movie> selectDirtyFilms();
    public void insertDirectors(List<Director> directors);
    public void insertActors(List<Actor> actors);
    public void markFilmAsClean(String idIMDB);
}
