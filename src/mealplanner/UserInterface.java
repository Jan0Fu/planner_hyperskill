package mealplanner;

import java.util.Scanner;

public class UserInterface {

    private final Scanner scan;

    public UserInterface(Scanner scan) {
        this.scan = scan;
    }

    public void start() {

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String categoryString = scan.nextLine().toUpperCase();
        Category category = Category.valueOf(categoryString);
        System.out.println("Input the meal's name:");
        String name = scan.nextLine();
        System.out.println("Input the ingredients:");
        String[] ingredients = scan.nextLine().split(",");

        Meal meal = new Meal(name, category, ingredients);
        System.out.println();
        System.out.println(meal);
        System.out.println("The meal has been added!");
    }
}
