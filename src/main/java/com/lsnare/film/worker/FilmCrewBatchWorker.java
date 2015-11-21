package com.lsnare.film.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by lucian.snare on 11/20/2015.
 */
public class FilmCrewBatchWorker {
    public static final int RUN_INTERVAL = 1000 * 60 * 30;
    static Log log = LogFactory.getLog(FilmCrewBatchWorker.class);

    public static void main(String[] args) {
        while(true){
            try {
                //check film records that need to be updated
                log.info("Processing film updates...");
                //update all dirty records

                //sleep
                log.info("Film updates complete");
                Thread.sleep(RUN_INTERVAL);
            } catch (Exception e) {

            }
        }
    }

}
