/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.menuserver.entities;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author T4User
 */
public class Dish {
    private String name;

    public Dish(){}
    
    public Dish(String name){
        this.name = name;
    }
    
    public int insertToDB(Connection connection) throws SQLException{
        //Insert to database
        Statement stmt = connection.createStatement();
        String sql = String.format("INSERT INTO dishes (id, name) VALUES (NULL, '%s')", getName());
        stmt.executeUpdate(sql);
        
        //Get the id which got automatically assigned by the database
        stmt = connection.createStatement();
        sql = String.format("SELECT id FROM dishes WHERE name = '%s'", getName());
        ResultSet data = stmt.executeQuery(sql);
        data.next();
        int id = data.getInt("id");
        
        //Return the id
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
