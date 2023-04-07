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
    private final List<String > listWithDuplicates;
    private final Map<String, Integer> shoppingList;

    public SaveMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
        this.listWithDuplicates = new ArrayList<>();
        this.shoppingList = new HashMap<>();
    }

    public void save() throws SQLException {
        Statement statement1 = connection.createStatement();
        ResultSet rs1 = statement1.executeQuery("select meal_id from plan");
        if (rs1.next()) {
            System.out.println("Input a filename:");
            File file = new File(scan.nextLine());
            try {
                PrintWriter writer = new PrintWriter(file);
                do {
                    Statement statement2 = connection.createStatement();
                    int meal_id = rs1.getInt("meal_id");
                    ResultSet rs2 = statement2.executeQuery("select ingredient from ingredients where meal_id=%d".formatted(meal_id));
                    while (rs2.next()) {
                        String ingredient = rs2.getString("ingredient");
                        listWithDuplicates.add(ingredient);
                    }
                    statement2.close();
                }
                while (rs1.next());
                for (String key: listWithDuplicates) {
                    if (shoppingList.containsKey(key)) {
                        int count = shoppingList.get(key) + 1;
                        shoppingList.put(key, count);
                    } else {
                        shoppingList.put(key, 1);
                    }
                }
                for (String key: shoppingList.keySet()) {
                    if (shoppingList.get(key) > 1) {
                        writer.println(key + " x" + shoppingList.get(key));
                    } else {
                        writer.println(key);
                    }
                }
                writer.flush();
                writer.close();
                statement1.close();
                System.out.println("Saved!");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Unable to save. Plan your meals first.");
        }
    }
}
