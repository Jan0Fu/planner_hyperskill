package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scan;

    private final List<Meal> meals = new ArrayList<>();

    public UserInterface(Scanner scan) {
        this.scan = scan;
    }

    public void start() {
        boolean loopOn = true;

        while (loopOn) {
            System.out.println("What would you like to do (add, show, exit)?");
            String input = scan.nextLine();

            switch (input) {
                case "add" -> add();
                case "show" -> show();
                case "exit" -> {
                    loopOn = false;
                    System.out.println("Bye!");
                }
                default -> {
                }
            }
        }
    }

    public void add() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String name;
        Category category;
        String[] ingredients;

        while (true) {
            String categoryString = scan.nextLine().toUpperCase();
            if (!"BREAKFAST LUNCH DINNER".contains(categoryString) || categoryString.equals("")) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                category = Category.valueOf(categoryString);
                break;
            }
        }
        System.out.println("Input the meal's name:");

        while (true) {
            name = scan.nextLine();
            if (!name.matches("[a-zA-Z ]+")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                break;
            }
        }
        System.out.println("Input the ingredients:");

        while (true) {
            String input = scan.nextLine();
            if (input.equals("") || !input.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+|\\s*,\\s*[a-zA-Z]+)*$")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                ingredients = input.split(",");
                break;
            }
        }
        Meal meal = new Meal(name, category, ingredients);
        meals.add(meal);
        System.out.println("The meal has been added!");
    }

    public void show() {
        if (meals.size() == 0) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            for (Meal meal: meals) {
                System.out.println(meal);
            }
        }
    }
}
