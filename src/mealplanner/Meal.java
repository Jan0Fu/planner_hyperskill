package mealplanner;

import java.util.Arrays;

public class Meal {

    private String name;
    private Category category;
    private String[] ingredients;

    public Meal(String name, Category category, String[] ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String toString() {
        StringBuilder sbMeal = new StringBuilder();
        sbMeal.append("Category: ")
                .append(this.category.toString().toLowerCase())
                .append("\nName: ")
                .append(this.name)
                .append("\nIngredients:\n");
        Arrays.stream(ingredients).forEach(s -> sbMeal.append(s).append("\n"));

        return sbMeal.toString();
    }
}
