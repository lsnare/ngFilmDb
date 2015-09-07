package com.lsnare.film.dao.impl;

import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Film;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by lucian on 9/2/15.
 */
public class FilmDAOImplementation implements FilmDAO{

    String error = "";
    private DataSource dataSource;
    private Film film;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActors(Connection conn){

        String sqlActor = "INSERT INTO actor VALUES(?,?)";
        String sqlActorFilmRole = "INSERT INTO actor_film_role VALUES(?,?,?)";
        for(Film.Actor a : this.film.getActors()){
            try {
                PreparedStatement psActor = conn.prepareStatement(sqlActor);
                psActor.setString(1, a.getActorId());
                psActor.setString(2, a.getActorName());
                psActor.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                //if an actor already exists, we still need to create an actor film role link
                try {
                    PreparedStatement psActorFilmRole = conn.prepareStatement(sqlActorFilmRole);
                    psActorFilmRole.setString(1, a.getActorId());
                    psActorFilmRole.setString(2, this.film.getIdIMDB());
                    psActorFilmRole.setString(3, a.getCharacter());
                    psActorFilmRole.execute();
                } catch (Exception e) {
                    error += "\n" + e.getMessage();
                }
            }
        }
    }

    public void insertDirectors(Connection conn){
        String sqlDirector = "INSERT INTO director VALUES(?,?)";
        String sqlDirectorFilmAssignment = "INSERT INTO director_film_assignment VALUES(?,?)";
        for(Film.Director d : this.film.getDirectors()){
            try {
                PreparedStatement psDirector = conn.prepareStatement(sqlDirector);
                psDirector.setString(1, d.getNameId());
                psDirector.setString(2, d.getName());
                psDirector.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    PreparedStatement psDirectorFilmAssignment = conn.prepareStatement(sqlDirectorFilmAssignment);
                    psDirectorFilmAssignment.setString(1, d.getNameId());
                    psDirectorFilmAssignment.setString(2, this.film.getIdIMDB());
                    psDirectorFilmAssignment.execute();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void insertGenres(Connection conn){
        String sqlGenre = "INSERT INTO genre VALUES(?)";
        String sqlFilmGenre = "INSERT INTO film_genre VALUES(?,?)";
        for(String g : this.film.getGenres()){
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
                    psFilmGenre.setString(2, this.film.getIdIMDB());
                    psFilmGenre.execute();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void insert(Film film) {
        this.film = film;
        String sql = "INSERT INTO film VALUES(?, ?, ?, ?)";
        Connection conn = null;
        try {
            System.out.println("Before connection");
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, film.getIdIMDB());
            ps.setString(2, film.getTitle());
            ps.setString(3, film.getPlot());
            ps.setInt(4, film.getYear());
            ps.execute();
            insertActors(conn);
            insertDirectors(conn);
            ps.close();
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public Film selectFilms(String filmTitle) {
        String sql = "SELECT * FROM film WHERE title = ?";
        Connection conn = null;
        try {
            System.out.println("Before connection");
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, filmTitle);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

            }
            Film film = new Film(rs.getString("idIMDB"), rs.getString("title"), rs.getString("plot"), rs.getInt("year"));
            ps.close();
            return film;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return null;
    }



        /*String actorSql = "INSERT INTO ";
        try {
            connection = DatabaseUrl.extract().getConnection();

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            ArrayList<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add( "Read from DB: " + rs.getTimestamp("tick"));
            }

            attributes.put("results", output);
            return new ModelAndView(attributes, "db.ftl");
        } catch (Exception e) {
            attributes.put("message", "There was an error: " + e);
            return new ModelAndView(attributes, "error.ftl");
        } finally {
            if (connection != null) try{connection.close();} catch(SQLException e){}
        }*/

    }

