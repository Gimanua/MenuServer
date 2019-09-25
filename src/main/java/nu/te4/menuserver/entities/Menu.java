/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.menuserver.entities;

import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author T4User
 */
public class Menu {
    private List<Meal> meals;
    private int year;
    private int week;
    
    public Menu(){}
    
    public Menu(List<Meal> meals, int year, int week){
        this.meals = meals;
        this.year = year;
        this.week = week;
    }

    public void insertToDB(Connection connection, List<Integer> mealIds, int week, int year) throws SQLException{
        //Insert to database
        Statement stmt = connection.createStatement();
        String sql = String.format(
        "INSERT INTO menus (week, year, monday_meal_id, tuesday_meal_id, wednesday_meal_id, thursday_meal_id, friday_meal_id) VALUES"
        + " (%d, %d, %d, %d, %d, %d, %d)", week, year, mealIds.get(0), mealIds.get(1), mealIds.get(2), 
        mealIds.get(3), mealIds.get(4));
        stmt.executeUpdate(sql);
    }
    
    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
