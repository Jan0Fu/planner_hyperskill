package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

    Scanner scan = new Scanner(System.in);

    String DB_URL = "jdbc:postgresql:meals_db";
    String USER = "postgres";
    String PASS = "1111";
    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
    connection.setAutoCommit(true);
    UserInterface ui = new UserInterface(scan, connection);
    Statement statement = connection.createStatement();
//    statement.executeUpdate("drop table if exists ingredients");
//    statement.executeUpdate("drop table if exists meals");
    statement.executeUpdate("create table IF NOT EXISTS meals(" +
            "meal_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
            "category varchar(30)," +
            "meal varchar(30) NOT NULL" +
            ")");
    statement.executeUpdate("create table IF NOT EXISTS ingredients(" +
            "ingredient varchar(30) NOT NULL," +
            "ingredient_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
            "meal_id INT" +
            ")");
    statement.executeUpdate("ALTER TABLE ingredients ADD FOREIGN KEY(meal_id) REFERENCES meals(meal_id)");
    statement.close();
    ui.start(connection);
    connection.close();
    }
}