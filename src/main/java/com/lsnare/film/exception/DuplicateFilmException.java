package com.lsnare.film.exception;

/**
 * Created by lucian.snare on 10/4/2015.
 */
public class DuplicateFilmException extends Exception {
    public DuplicateFilmException(String title){
        super("The film " + title + " already exists in the database.");
    }
}
