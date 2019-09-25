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
public class Meal {
    private Dish mainDish;
    private Dish alternativeDish;

    public Meal(){}
    
    public Meal(Dish mainDish, Dish alternativeDish){
        this.mainDish = mainDish;
        this.alternativeDish = alternativeDish;
    }
    
    public int insertToDB(Connection connection, int mainDishId, int altDishId) throws SQLException{
        //Insert to database
        Statement stmt = connection.createStatement();
        String sql = String.format(
                "INSERT INTO meals (id, main_dish_id, alt_dish_id) VALUES (NULL, %d, %d)", 
                mainDishId, altDishId);
        stmt.executeUpdate(sql);
        
        //Get the id which got automatically assigned by the database
        stmt = connection.createStatement();
        sql = String.format(
            "SELECT id FROM meals WHERE main_dish_id = %d AND alt_dish_id = %d",
            mainDishId, altDishId);
        ResultSet data = stmt.executeQuery(sql);
        data.next();
        int id = data.getInt("id");
        
        return id;
    }
    
    public Dish getMainDish() {
        return mainDish;
    }

    public void setMainDish(Dish mainDish) {
        this.mainDish = mainDish;
    }

    public Dish getAlternativeDish() {
        return alternativeDish;
    }

    public void setAlternativeDish(Dish alternativeDish) {
        this.alternativeDish = alternativeDish;
    }
}
