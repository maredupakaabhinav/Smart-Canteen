package com.smartcanteen.model;

/**
 * One food item inside an order. "price" is the unit price at the time the order was placed.
 */
public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int foodId;
    private int quantity;
    private double price;
    private String foodName;   // filled in when the item is read from the database (JOIN)

    public OrderItem() {
    }

    public OrderItem(int foodId, String foodName, int quantity, double price) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    public double getSubtotal() {
        return Math.round(price * quantity * 100.0) / 100.0;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
