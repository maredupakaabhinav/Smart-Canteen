package com.smartcanteen.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * One item on the canteen menu (maps to the food_items table).
 */
public class FoodItem implements Serializable {

    /** The only categories the canteen uses. */
    public static final List<String> CATEGORIES =
            Collections.unmodifiableList(Arrays.asList("Breakfast", "Snacks", "Meals", "Beverages"));

    private int foodId;
    private String name;
    private String description;
    private String category;
    private double price;
    private String image;
    private boolean available;

    public FoodItem() {
        this.available = true;
        this.description = "";
        this.image = "";
    }

    public FoodItem(int foodId, String name, String description, String category,
                    double price, String image, boolean available) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.available = available;
    }

    /** A simple emoji shown when the food has no image URL. */
    public String getIcon() {
        if ("Breakfast".equals(category)) {
            return "🍳";   // cooking
        } else if ("Snacks".equals(category)) {
            return "🥪";   // sandwich
        } else if ("Meals".equals(category)) {
            return "🍛";   // curry and rice
        } else if ("Beverages".equals(category)) {
            return "☕";         // hot drink
        }
        return "🍽";       // plate
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
