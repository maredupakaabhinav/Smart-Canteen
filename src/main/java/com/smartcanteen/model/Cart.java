package com.smartcanteen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The shopping cart. One Cart object is kept in each user's HttpSession.
 * COLLECTIONS: items are stored in a Map (foodId -> CartItem) so a food item can appear only once.
 */
public class Cart implements Serializable {

    public static final int MAX_QUANTITY_PER_ITEM = 10;

    private final Map<Integer, CartItem> items = new LinkedHashMap<>();

    /** Adds a food item. If it is already in the cart, the quantities are added together. */
    public void addItem(FoodItem food, int quantity) {
        checkQuantity(quantity);   // the amount being added must be valid on its own
        CartItem existing = items.get(food.getFoodId());
        int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        checkQuantity(newQuantity);

        if (existing == null) {
            items.put(food.getFoodId(), new CartItem(food, quantity));
        } else {
            existing.setQuantity(newQuantity);
        }
    }

    /** Sets the quantity of an item that is already in the cart. */
    public void updateQuantity(int foodId, int quantity) {
        checkQuantity(quantity);
        CartItem item = items.get(foodId);
        if (item == null) {
            throw new IllegalArgumentException("That item is not in your cart.");
        }
        item.setQuantity(quantity);
    }

    public void removeItem(int foodId) {
        items.remove(foodId);
    }

    public void clear() {
        items.clear();
    }

    public CartItem getItem(int foodId) {
        return items.get(foodId);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Total number of pieces (used for the cart badge in the navbar). */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    private void checkQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY_PER_ITEM) {
            throw new IllegalArgumentException("Please enter a valid quantity (1 to "
                    + MAX_QUANTITY_PER_ITEM + ").");
        }
    }
}
