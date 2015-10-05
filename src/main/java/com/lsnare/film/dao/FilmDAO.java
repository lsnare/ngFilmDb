package com.lsnare.film.dao;
import com.lsnare.film.exception.DuplicateFilmException;
import com.lsnare.film.model.Film;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by lucian on 9/3/15.
 */
public interface FilmDAO {
    public void insert(Film film) throws DuplicateFilmException;
    public List<Film> selectFilmsByTitle(String title);
    public Map<String, String> selectRolesForActor(String actorName);
}
