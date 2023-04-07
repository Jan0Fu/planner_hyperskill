package mealplanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddMode {

    private final Scanner scan;
    private final Connection connection;

    public AddMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
    }

    public void add() throws SQLException {

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String mealName;
        Category category;
        List<String> ingredients = new ArrayList<>();

        while (true) {
            String categoryString = scan.nextLine().toUpperCase();
            if (!"BREAKFAST LUNCH DINNER".contains(categoryString)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                category = Category.valueOf(categoryString);
                break;
            }
        }
        System.out.println("Input the meal's name:");

        while (true) {
            mealName = scan.nextLine();
            if (!mealName.matches("[a-zA-Z ]+")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                break;
            }
        }
        System.out.println("Input the ingredients:");

        while (true) {
            String input = scan.nextLine();
            if (!input.matches("([a-zA-Z]+,? ?)+(?<!,)(?<! )")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                String[] parts = input.split(",");
                for (String i: parts) {
                    ingredients.add(i.trim());
                }
                break;
            }
        }
        Statement statement = connection.createStatement();
        statement.executeUpdate("insert into meals (category,meal) values ('%s','%s')".formatted(category, mealName));
        ResultSet rs = statement.executeQuery("select meal_id from meals where meal='%s'".formatted(mealName));
        while (rs.next()) {
            int meal_id = rs.getInt("meal_id");
            for (String ingredient : ingredients) {
                Statement statement1 = connection.createStatement();
                statement1.executeUpdate("insert into ingredients (ingredient, meal_id) values ('%s',%d)".formatted(ingredient, meal_id));
                statement1.close();
            }
        }
        statement.close();
        System.out.println("The meal has been added!");
    }
}
