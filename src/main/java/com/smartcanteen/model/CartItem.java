package com.smartcanteen.model;

import java.io.Serializable;

/**
 * One line in the cart: a food item and how many of it.
 */
public class CartItem implements Serializable {

    private FoodItem foodItem;
    private int quantity;

    public CartItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public int getFoodId() {
        return foodItem.getFoodId();
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return Math.round(foodItem.getPrice() * quantity * 100.0) / 100.0;
    }
}
