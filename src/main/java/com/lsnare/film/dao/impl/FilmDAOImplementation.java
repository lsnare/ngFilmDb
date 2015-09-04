package com.lsnare.film.dao.impl;

import com.lsnare.film.dao.FilmDAO;
import com.lsnare.film.model.Film;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by lucian on 9/2/15.
 */
public class FilmDAOImplementation implements FilmDAO{

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Film film){

        String sql = "INSERT INTO film VALUES(?, ?, ?, ?)";
        Connection conn = null;
        try{
            System.out.println("Before connection");
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, film.getIdIMDB());
            ps.setString(2, film.getTitle());
            ps.setString(3, film.getPlot());
            ps.setInt(4, film.getYear());
            ps.execute();
            ps.close();
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
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

}
