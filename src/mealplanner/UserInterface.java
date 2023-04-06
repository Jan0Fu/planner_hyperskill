package mealplanner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scan;
    private final AddMode addMode;
    private final ShowMode showMode;

    public UserInterface(Scanner scan, Connection connection) {
        this.scan = scan;
        this.addMode = new AddMode(scan, connection);
        this.showMode = new ShowMode(scan, connection);
    }

    public void start(Connection connection) throws SQLException {

        boolean loopOn = true;
        while (loopOn) {
            System.out.println("What would you like to do (add, show, exit)?");
            String input = scan.nextLine();

            switch (input) {
                case "add" -> addMode.add();
                case "show" -> showMode.show();
                case "exit" -> {
                    connection.close();
                    loopOn = false;
                    System.out.println("Bye!");
                }
            }
        }
    }
}
