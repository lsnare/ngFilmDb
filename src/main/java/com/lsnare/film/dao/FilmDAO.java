package com.lsnare.film.dao;
import com.lsnare.film.model.Film;

import java.util.List;

/**
 * Created by lucian on 9/3/15.
 */
public interface FilmDAO {
    public void insert(Film film);
    public List<Film> selectFilms(String title);
}
