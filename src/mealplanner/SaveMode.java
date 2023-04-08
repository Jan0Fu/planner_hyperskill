package mealplanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SaveMode {

    private final Scanner scan;
    private final Connection connection;
    private final Map<String, Integer> shoppingList;

    public SaveMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
        this.shoppingList = new HashMap<>();
    }

    public void save() throws SQLException {
        Statement statement1 = connection.createStatement();
        ResultSet rs1 = statement1.executeQuery("select meal_id from plan");
        if (!rs1.next()) {
            System.out.println("Unable to save. Plan your meals first.");
            return;
        }
        System.out.println("Input a filename:");
        File file = new File(scan.nextLine());

        do {
            Statement statement2 = connection.createStatement();
            int meal_id = rs1.getInt("meal_id");
            ResultSet rs2 = statement2.executeQuery("select ingredient from ingredients where meal_id=%d".formatted(meal_id));

            while (rs2.next()) {
                String ingredient = rs2.getString("ingredient");
                if (shoppingList.containsKey(ingredient)) {
                    int count = shoppingList.get(ingredient) + 1;
                    shoppingList.put(ingredient, count);
                } else {
                    shoppingList.put(ingredient, 1);
                }
            }
            statement2.close();
        }
        while (rs1.next());
        statement1.close();
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String key: shoppingList.keySet()) {
                if (shoppingList.get(key) > 1) {
                    writer.println(key + " x" + shoppingList.get(key));
                } else {
                    writer.println(key);
                }
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
        System.out.println("Saved!");
    }
}
