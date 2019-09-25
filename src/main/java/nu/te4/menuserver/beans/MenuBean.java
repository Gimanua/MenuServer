package nu.te4.menuserver.beans;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import nu.te4.menuserver.ConnectionFactory;
import nu.te4.menuserver.entities.Dish;
import nu.te4.menuserver.entities.Meal;
import nu.te4.menuserver.entities.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author T4User
 */
@Stateless
public class MenuBean {
    
    public Menu getMenu(){
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        int year = now.get(Calendar.YEAR);
        int week = now.get(Calendar.WEEK_OF_YEAR);
        
        Menu menu = getMenuFromDB(week, year);
        if(menu == null){
            menu = getMenuFromWeb(week, year);
            if(menu != null){
                storeInDB(menu, week, year);
            }
        }
        
        return menu;
    }
    
    // Gets the menu from the database
    private Menu getMenuFromDB(int week, int year){
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement stmt = connection.createStatement();
            String sql = String.format(
                    "SELECT * FROM menus WHERE week = %d AND year = %d", 
                    week, year);
            ResultSet data = stmt.executeQuery(sql);
            boolean exists = data.next();
            if(!exists)
                return null;
            
            List<Integer> mealIds = new ArrayList();
            mealIds.add(data.getInt("monday_meal_id"));
            mealIds.add(data.getInt("tuesday_meal_id"));
            mealIds.add(data.getInt("wednesday_meal_id"));
            mealIds.add(data.getInt("thursday_meal_id"));
            mealIds.add(data.getInt("friday_meal_id"));
            
            List<Meal> meals = getMealsFromDB(mealIds, connection);
            if(meals == null)
                return null;
            
            return new Menu(meals, year, week);
            
        } catch (Exception e) {
            System.out.println("MenuBean.getMenuFromDB: " + e.getMessage());
        }
        
        return null;
    }
    
    private List<Meal> getMealsFromDB(List<Integer> ids, Connection connection) throws SQLException{
        List<Meal> meals = new ArrayList();
        
        for(int id : ids){
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM meals WHERE id = " + id;
            ResultSet data = stmt.executeQuery(sql);
            boolean exists = data.next();
            if(!exists)
                return null;
            
            List<Integer> dishIds = new ArrayList();
            dishIds.add(data.getInt("main_dish_id"));
            dishIds.add(data.getInt("alt_dish_id"));
            
            List<Dish> mealDishes = getDishesFromDB(dishIds, connection);
            if(mealDishes == null)
                return null;
            
            meals.add(new Meal(mealDishes.get(0), mealDishes.get(1)));
        }
        
        
        return meals;
    }
    
    private List<Dish> getDishesFromDB(List<Integer> ids, Connection connection) throws SQLException{
        List<Dish> dishes = new ArrayList();
        
        for(int id : ids){
            Statement stmt = connection.createStatement();
            String sql = "SELECT name FROM dishes WHERE id = " + id;
            ResultSet data = stmt.executeQuery(sql);
            boolean exists = data.next();
            if(!exists)
                return null;
            
            String name = data.getString("name");
            Dish dish = new Dish(name);
            dishes.add(dish);
        }
        
        return dishes;
    }
    
    // Gets the menu from the website: https://skolmaten.se/teknikum/
    private Menu getMenuFromWeb(int week, int year){
        try {
            Document doc = Jsoup.connect("https://skolmaten.se/teknikum/").get();
            Elements mealElements = doc.select("#weeks .row");
            List<Meal> meals = new ArrayList();
            
            for(Element meal : mealElements){
                Elements dishes = meal.select(".items span");
                Dish mainDish = new Dish(dishes.first().html());
                Dish alternativeDish = new Dish(dishes.last().html());
                meals.add(new Meal(mainDish, alternativeDish));
            }
            
            return new Menu(meals, year, week);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("MenuBean.getMenuFromWeb: " + e.getMessage());
        }
        return null;
    }
    
    private boolean storeInDB(Menu menu, int week, int year){
        try (Connection connection = ConnectionFactory.getConnection()) {
            List<Integer> mealIds = new ArrayList();
            
            for(Meal meal : menu.getMeals()){
                //Insert main dish
                Dish mainDish = meal.getMainDish();
                int mainDishId = mainDish.insertToDB(connection);
                
                //Insert alt dish
                Dish altDish = meal.getAlternativeDish();
                int altDishId = altDish.insertToDB(connection);
                
                mealIds.add(meal.insertToDB(connection, mainDishId, altDishId));
            }
            
            menu.insertToDB(connection, mealIds, week, year);
            return true;
        } catch (Exception e) {
            System.out.println("MenuBean.storeInDB: " + e.getMessage());
        }
        return false;
    }
}
