package mealplanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ShowMode {

    private final Connection connection;
    private final Scanner scan;

    public ShowMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
    }

    public void show() throws SQLException {

        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        Category category;
        while (true) {
            String categoryString = scan.nextLine().toUpperCase();
            if (!"BREAKFAST LUNCH DINNER".contains(categoryString)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                category = Category.valueOf(categoryString);
                break;
            }
        }
        Statement statement1 = connection.createStatement();
        ResultSet rs1 = statement1.executeQuery("select * from meals where category='%s' ORDER BY meal_id".formatted(category));

        if (rs1.next()) {
            Statement statement2 = connection.createStatement();
            System.out.println("Category: " + category.toString().toLowerCase());
            do {
                System.out.println("\nName: " + rs1.getString("meal"));
                int meal_id = rs1.getInt("meal_id");
                ResultSet rs2 = statement2.executeQuery("select * from ingredients WHERE meal_id=%d ORDER BY ingredient_id".formatted(meal_id));
                System.out.println("Ingredients:");
                while (rs2.next()) {
                    System.out.println(rs2.getString("ingredient"));
                }
            }
            while (rs1.next());
            statement2.close();
        } else {
            System.out.println("No meals found.");
        }
        statement1.close();
    }
}
