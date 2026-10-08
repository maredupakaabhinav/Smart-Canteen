package com.smartcanteen.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A placed order (maps to the orders table).
 */
public class Order {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    private int orderId;
    private int userId;
    private double totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime pickupTime;
    private OrderStatus status;

    // Extra data that is filled in when needed
    private List<OrderItem> items = new ArrayList<>();
    private Payment payment;
    private String customerName;
    private String customerCollegeId;

    public Order() {
    }

    /** The order number shown to people, e.g. SC1001. */
    public String getDisplayId() {
        return "SC" + orderId;
    }

    /** "Veg Sandwich x2, Coffee x1" */
    public String getItemsSummary() {
        StringBuilder summary = new StringBuilder();
        for (OrderItem item : items) {
            if (summary.length() > 0) {
                summary.append(", ");
            }
            summary.append(item.getFoodName()).append(" x").append(item.getQuantity());
        }
        return summary.toString();
    }

    public String getOrderDateText() {
        return orderDate.format(DATE_FORMAT) + ", " + orderDate.format(TIME_FORMAT);
    }

    /** Short version for tables: 08 Oct, 10:31 AM */
    public String getOrderDateShortText() {
        return orderDate.format(DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)) + ", " + orderDate.format(TIME_FORMAT);
    }

    public String getPickupDateText() {
        return pickupTime.format(DATE_FORMAT);
    }

    public String getPickupTimeText() {
        return pickupTime.format(TIME_FORMAT);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCollegeId() {
        return customerCollegeId;
    }

    public void setCustomerCollegeId(String customerCollegeId) {
        this.customerCollegeId = customerCollegeId;
    }
}
