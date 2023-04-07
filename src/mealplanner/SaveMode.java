package mealplanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SaveMode {

    private final Scanner scan;
    private final Connection connection;

    public SaveMode(Scanner scan, Connection connection) {
        this.scan = scan;
        this.connection = connection;
    }

    public void save() throws SQLException {
        Statement statement1 = connection.createStatement();
        ResultSet rs1 = statement1.executeQuery("select meal_id from plan");
        if (rs1.next()) {
            System.out.println("Input a filename:");
            File file = new File(scan.nextLine());
            try {
                PrintWriter printWriter = new PrintWriter(file);
                do {
                    Statement statement2 = connection.createStatement();
                    int meal_id = rs1.getInt("meal_id");
                    ResultSet rs2 = statement2.executeQuery("select ingredient from ingredients where meal_id=%d".formatted(meal_id));
                    while (rs2.next()) {
                        printWriter.println(rs2.getString("ingredient"));
                    }
                    statement2.close();
                }
                while (rs1.next());
                printWriter.flush();
                printWriter.close();
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
