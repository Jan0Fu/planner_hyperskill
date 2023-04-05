package mealplanner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scan;
    private final Connection connection;

    public UserInterface(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
    }

    public void start(Connection connection) throws SQLException {

        boolean loopOn = true;
        while (loopOn) {
            System.out.println("What would you like to do (add, show, exit)?");
            String input = scan.nextLine();

            switch (input) {
                case "add" -> add();
                case "show" -> show();
                case "exit" -> {
                    connection.close();
                    loopOn = false;
                    System.out.println("Bye!");
                }
            }
        }
    }

    public void add() throws SQLException {

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String mealName;
        Category category;
        String[] ingredients;

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
            String stripped = input.replaceAll("\\s", "");
            if (!input.matches("([a-zA-Z]+,? ?)+(?<!,)(?<! )")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                ingredients = stripped.split(",");
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

    public void show() throws SQLException {
        Statement statement1 = connection.createStatement();
        ResultSet rs1 = statement1.executeQuery("select * from meals");

        if (rs1.next()) {
            Statement statement2 = connection.createStatement();
            do {
                System.out.println("\nCategory: " + rs1.getString("category"));
                System.out.println("Name: " + rs1.getString("meal"));
                int meal_id = rs1.getInt("meal_id");
                ResultSet rs2 = statement2.executeQuery("select * from ingredients WHERE meal_id = %d".formatted(meal_id));
                System.out.println("Ingredients:");
                while (rs2.next()) {
                    if (meal_id == rs2.getInt("meal_id")) {
                        System.out.println(rs2.getString("ingredient"));
                    }
                }
            }
            while (rs1.next());
            System.out.println();
            statement2.close();
        } else {
            System.out.println("\nNo meals saved. Add a meal first.");
        }
        statement1.close();

    }
}
