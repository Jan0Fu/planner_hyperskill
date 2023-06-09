package mealplanner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scan;
    private final AddMode addMode;
    private final ShowMode showMode;
    private final PlanMode planMode;
    private final SaveMode saveMode;

    public UserInterface(Scanner scan, Connection connection) {
        this.scan = scan;
        this.addMode = new AddMode(scan, connection);
        this.showMode = new ShowMode(scan, connection);
        this.planMode = new PlanMode(scan, connection);
        this.saveMode = new SaveMode(scan, connection);
    }

    public void start(Connection connection) throws SQLException {

        boolean loopOn = true;
        while (loopOn) {
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String input = scan.nextLine();

            switch (input) {
                case "add" -> addMode.add();
                case "show" -> showMode.show();
                case "plan" -> planMode.plan();
                case "save" -> saveMode.save();
                case "exit" -> {
                    connection.close();
                    loopOn = false;
                    System.out.println("Bye!");
                }
            }
        }
    }
}
