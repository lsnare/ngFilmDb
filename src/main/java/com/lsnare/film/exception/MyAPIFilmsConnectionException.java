package com.lsnare.film.exception;

/**
 * Created by lucian.snare on 10/12/2015.
 */
public class MyAPIFilmsConnectionException extends Exception {
    public MyAPIFilmsConnectionException(String message){
        super("Encountered an error while searching MyAPIFilms.com: " + message);
    }
}
