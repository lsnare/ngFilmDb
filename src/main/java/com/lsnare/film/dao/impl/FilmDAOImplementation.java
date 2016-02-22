package com.lsnare.film.dao.impl;

import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.exception.DuplicateFilmException;
import com.lsnare.film.model.Actor;
import com.lsnare.film.model.Director;
import com.lsnare.film.model.Movie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucian on 9/2/15.
 */
public class FilmDAOImplementation implements FilmDAO {

    String error = "";
    private DataSource dataSource;
    private Movie movie;
    Log log = LogFactory.getLog(FilmDAOImplementation.class);
    Connection conn = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActors(Connection conn) {

        String sqlActor = "INSERT INTO actor VALUES(?,?)";
        String sqlActorFilmRole = "INSERT INTO actor_film_role VALUES(?,?,?)";
        for (Actor a : this.movie.getActors()) {
            try {
                PreparedStatement psActor = conn.prepareStatement(sqlActor);
                psActor.setString(1, a.getActorId());
                psActor.setString(2, a.getActorName());
                psActor.execute();
            } catch (SQLException e) {
                log.info(e.getMessage());
            } finally {
                //if an actor already exists, we still need to create an actor movie role link
                try {
                    PreparedStatement psActorFilmRole = conn.prepareStatement(sqlActorFilmRole);
                    psActorFilmRole.setString(1, a.getActorId());
                    psActorFilmRole.setString(2, this.movie.getIdIMDB());
                    psActorFilmRole.setString(3, a.getCharacter());
                    psActorFilmRole.execute();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.info("Finished inserting actors");
    }

    public void insertActors(List<Actor> actors) {

        String sqlActor = "INSERT INTO actor VALUES(?,?)";
        String sqlActorFilmRole = "INSERT INTO actor_film_role VALUES(?,?,?)";
        try{
            Connection conn = dataSource.getConnection();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        for (Actor a : actors) {
            try {
                PreparedStatement psActor = conn.prepareStatement(sqlActor);
                psActor.setString(1, a.getActorId());
                psActor.setString(2, a.getActorName());
                psActor.execute();
            } catch (SQLException e) {
                log.info(e.getMessage());
            } finally {
                //if an actor already exists, we still need to create an actor movie role link
                try {
                    PreparedStatement psActorFilmRole = conn.prepareStatement(sqlActorFilmRole);
                    psActorFilmRole.setString(1, a.getActorId());
                    psActorFilmRole.setString(2, this.movie.getIdIMDB());
                    psActorFilmRole.setString(3, a.getCharacter());
                    psActorFilmRole.execute();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.info("Finished inserting actors");
    }

    public void insertDirectors(Connection conn) {
        String sqlDirector = "INSERT INTO director VALUES(?,?)";
        String sqlDirectorFilmAssignment = "INSERT INTO director_film_assignment VALUES(?,?)";
        for (Director d : this.movie.getDirectors()) {
            try {
                PreparedStatement psDirector = conn.prepareStatement(sqlDirector);
                psDirector.setString(1, d.getNameId());
                psDirector.setString(2, d.getName());
                psDirector.execute();
            } catch (SQLException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    PreparedStatement psDirectorFilmAssignment = conn.prepareStatement(sqlDirectorFilmAssignment);
                    psDirectorFilmAssignment.setString(1, d.getNameId());
                    psDirectorFilmAssignment.setString(2, this.movie.getIdIMDB());
                    psDirectorFilmAssignment.execute();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.info("Finished inserting directors");
    }

    public void insertDirectors(List<Director> directors) {
        String sqlDirector = "INSERT INTO director VALUES(?,?)";
        String sqlDirectorFilmAssignment = "INSERT INTO director_film_assignment VALUES(?,?)";
        try {
            Connection conn = dataSource.getConnection();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        for (Director d : directors) {
            try {
                PreparedStatement psDirector = conn.prepareStatement(sqlDirector);
                psDirector.setString(1, d.getNameId());
                psDirector.setString(2, d.getName());
                psDirector.execute();
            } catch (SQLException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    PreparedStatement psDirectorFilmAssignment = conn.prepareStatement(sqlDirectorFilmAssignment);
                    psDirectorFilmAssignment.setString(1, d.getNameId());
                    psDirectorFilmAssignment.setString(2, this.movie.getIdIMDB());
                    psDirectorFilmAssignment.execute();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.info("Finished inserting directors");
    }

    public void insertGenres(Connection conn) {
        String sqlGenre = "INSERT INTO genre VALUES(?)";
        String sqlFilmGenre = "INSERT INTO film_genre VALUES(?,?)";
        for (String g : this.movie.getGenres()) {
            try {
                PreparedStatement psGenre = conn.prepareStatement(sqlGenre);
                psGenre.setString(1, g);
                psGenre.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    PreparedStatement psFilmGenre = conn.prepareStatement(sqlFilmGenre);
                    psFilmGenre.setString(1, g);
                    psFilmGenre.setString(2, this.movie.getIdIMDB());
                    psFilmGenre.execute();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void insert(Movie movie) throws DuplicateFilmException {
        this.movie = movie;
        String sql = "INSERT INTO film VALUES(?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movie.getIdIMDB());
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getPlot());
            ps.setString(4, movie.getYear());
            ps.execute();
            log.info("Inserted movie");
            insertActors(conn);
            insertDirectors(conn);
            ps.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"film_pkey\"")) {
                throw new DuplicateFilmException(this.movie.getTitle());
            }
        }
    }

    //Selects all films that are missing actors, directors, etc.
    public List<Movie> selectDirtyFilms(){
        String sql = "SELECT * FROM film WHERE isDataComplete = FALSE";
        List<Movie> movies = new ArrayList<>();

        try {
            Connection conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setIdIMDB(rs.getString("idIMDB"));
                log.info("Adding " + movie.getTitle() + " to results");
                movies.add(movie);
            }
            log.info("Search complete");
            log.debug("Found " + movies.size() + " movies that need to be completed");
            ps.close();
        } catch (Exception e) {
            log.error("Dirty movie select error: " + e + "\n");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return movies;
    }

    public void markFilmAsClean(String idIMDB){
        String sql = "UPDATE film WHERE idIMDB = ? SET isDataComplete = TRUE";

        try {
            Connection conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idIMDB);
            ResultSet rs = ps.executeQuery();
            log.info("Movie marked as clean");
            ps.close();
        } catch (Exception e) {
            log.error("Movie update error: " + e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Error marking films as clean: " + e.getMessage());
                }
            }
        }
    }

    public List<Movie> selectFilmsByTitle(String filmTitle) {
        String sql = "SELECT * FROM film WHERE UPPER(title) LIKE UPPER(?)";
        List<Movie> movies = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + filmTitle + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setIdIMDB(rs.getString("idIMDB"));
                movie.setTitle(rs.getString("title"));
                movie.setPlot(rs.getString("plot"));
                movie.setYear(rs.getString("year"));
                movie.setActors(selectActorsForFilm(movie.getTitle()));
                movie.setDirectors(selectDirectorsForFilm(movie.getTitle()));
                log.info("Adding " + movie.getTitle() + " to results");
                log.info("Found the following actors: " + movie.getActors());
                movies.add(movie);
            }
            log.info("Search complete");
            log.debug("Found " + movies.size() + " movies related to the search for titles similar to " + filmTitle);
            ps.close();
        } catch (Exception e) {
            log.error("Movie select error: " + e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return movies;
    }

    public Map<String, Map<String, String>> selectRolesForActor(String actorName) {
        String sql = "SELECT a.actorName, r.role, f.title "
                + "FROM actor a "
                + "INNER JOIN actor_film_role r on r.actorId = a.actorId "
                + "INNER JOIN film f on f.idIMDB = r.idIMDB "
                + "WHERE UPPER(a.actorName) LIKE UPPER(?)";
        Map<String, Map<String, String>> roles = new HashMap();

        try {
            conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + actorName + "%");
            log.info("Searching for " + actorName + " in database");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String currentActorName = rs.getString("actorName");
                String currentFilmTitle = rs.getString("title");
                String currentRole = rs.getString("role");

                log.info("Working with actor: " + currentActorName + " role: " + currentRole + " movie: " + currentFilmTitle);
                if (roles.containsKey(currentActorName)) {
                    //Update the mappings for actors we have already looped over
                    Map<String, String> rolesForCurrentActor = roles.get(currentActorName);
                    log.info("Adding the role of " + currentRole + " in the movie " + currentFilmTitle + " for " + currentActorName);
                    rolesForCurrentActor.put(currentFilmTitle, currentRole);
                    roles.put(currentActorName, rolesForCurrentActor);
                    log.info("mapping for actor " + currentActorName + ": " + roles.get(currentActorName));
                } else {
                    Map currentRoleMapping = new HashMap<String, String>();
                    currentRoleMapping.put(currentFilmTitle, currentRole);
                    roles.put(currentActorName, currentRoleMapping);
                }

            }
            log.info("Search complete");
            log.debug("Found " + roles.size() + " roles related to the search for actor " + actorName);
            ps.close();
        } catch (Exception e) {
            log.error("Movie select error: " + e);
            log.error("Specific error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return roles;
    }

    public List<Actor> selectActorsForFilm(String filmTitle) {
        String sql = "SELECT a.actorName "
                + "FROM actor a "
                + "INNER JOIN actor_film_role r on r.actorId = a.actorId "
                + "INNER JOIN film f on f.idIMDB = r.idIMDB "
                + "WHERE UPPER(f.title) LIKE UPPER(?)";
        List<Actor> actors = new ArrayList();

        try {
            conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + filmTitle + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                actors.add(new Actor(rs.getString("actorName")));
                log.info("Adding actor " + actors.get(actors.size() - 1).getActorName() + " to cast");
            }
            log.info("Search complete");
            log.debug("Found " + actors.size() + " actors related to the movie " + filmTitle);
            ps.close();
        } catch (Exception e) {
            log.error("Actor select error: " + e);
            log.error("Specific error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return actors;
    }

    public List<Director> selectDirectorsForFilm(String filmTitle) {
        String sql = "SELECT d.name "
                + "FROM director d "
                + "INNER JOIN director_film_assignment dfa on dfa.directorId = d.nameId "
                + "INNER JOIN film f on f.idIMDB = dfa.filmId "
                + "WHERE UPPER(f.title) LIKE UPPER(?)";
        List<Director> directors = new ArrayList();

        try {
            conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + filmTitle + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                directors.add(new Director(rs.getString("name")));
                log.info("Adding director " + directors.get(directors.size() - 1).getName() + " to list");
            }
            log.info("Search complete");
            log.debug("Found " + directors.size() + " directors related to the movie " + filmTitle);
            ps.close();
        } catch (Exception e) {
            log.error("Actor select error: " + e);
            log.error("Specific error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return directors;
    }

    public Map<String, Map<String, String>> selectFilmsForDirector(String directorName){
        String sql = "SELECT d.name, f.title, f.year "
                + "FROM director d "
                + "INNER JOIN director_film_assignment dfa on dfa.directorId = d.nameId "
                + "INNER JOIN film f on f.idIMDB = dfa.filmId "
                + "WHERE UPPER(d.name) LIKE UPPER(?)";
        Map<String, Map<String, String>> filmsByDirector = new HashMap();

        try {
            conn = dataSource.getConnection();
            log.info("Connection established");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + directorName + "%");
            log.info("Searching for films by " + directorName + " in database");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String currentDirectorName = rs.getString("name");
                String currentFilmTitle = rs.getString("title");
                String currentYear = rs.getString("year");

                log.info("Working with director " + currentDirectorName + " who filmed " + currentFilmTitle);
                if (filmsByDirector.containsKey(currentDirectorName)) {
                    //Update the mappings for actors we have already looped over
                    Map<String, String> currentFilmToYearMapping = filmsByDirector.get(currentDirectorName);
                    log.info("Adding the film " + currentFilmTitle + " filmed by " + currentDirectorName);
                    currentFilmToYearMapping.put(currentFilmTitle, currentYear);
                    filmsByDirector.put(currentDirectorName, currentFilmToYearMapping);
                    log.info("Mapping for director " + currentDirectorName + ": " + filmsByDirector.get(currentDirectorName));
                } else {
                    Map currentFilmToYearMapping = new HashMap<String, String>();
                    currentFilmToYearMapping.put(currentFilmTitle, currentYear);
                    filmsByDirector.put(currentDirectorName, currentFilmToYearMapping);
                }
            }
            log.info("Search complete");
            log.debug("Found " + filmsByDirector.size() + " films related to the search for director " + directorName);
            ps.close();
        } catch (Exception e) {
            log.error("Error searching films by director: " + e);
            log.error("Specific error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("error dao: " + e.getMessage());
                }
            }
        }
        return filmsByDirector;
    }
}

