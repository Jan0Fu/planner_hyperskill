package mealplanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanMode {

    private final Scanner scan;
    private final Connection connection;

    public PlanMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
    }

    public void plan() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM plan");
        statement.close();
        for (var day: Weekdays.values()) {
            System.out.println(day);

            planMeals(day, Category.BREAKFAST);
            planMeals(day, Category.LUNCH);
            planMeals(day, Category.DINNER);
            System.out.printf("Yeah! We planned the meals for %s.\n\n", day);
        }
        printDailyMeals();
    }

    private void planMeals(Weekdays day, Category category) throws SQLException {
        List<String> meals = new ArrayList<>();
        Statement statement1 = connection.createStatement();
        ResultSet rsBreakfast = statement1.executeQuery("select * from meals where category='%s' ORDER BY meal".formatted(category));

        while (rsBreakfast.next()) {
            String mealName = rsBreakfast.getString("meal");
            meals.add(mealName);
            System.out.println(mealName);
        }
        statement1.close();
        System.out.printf("Choose the %s for %s from the list above:\n", category.toString().toLowerCase(), day);

        while (true) {
            String input = scan.nextLine().toLowerCase();
            if (!meals.contains(input)) {
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            } else {
                Statement statement2 = connection.createStatement();
                ResultSet rs = statement2.executeQuery("select meal_id from meals where meal='%s'".formatted(input));
                int meal_id = 0;
                if (rs.next()) {
                    meal_id = rs.getInt("meal_id");
                }
                statement2.executeUpdate("insert into plan values('%s','%s','%s',%d)".formatted(input,category,day,meal_id));
                statement2.close();
                break;
            }
        }
    }

    private void printDailyMeals() throws SQLException {
        for (var day: Weekdays.values()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select meal_option,category from plan where weekday='%s'".formatted(day));
            System.out.println(day);
            while (rs.next()) {
                String tempCategory = rs.getString("category");
                String capitalized = tempCategory.substring(0,1).toUpperCase() + tempCategory.substring(1).toLowerCase();
                System.out.println(capitalized + ": " + rs.getString("meal_option"));
            }
            System.out.println();
        }
    }
}
