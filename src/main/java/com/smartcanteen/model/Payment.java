package com.smartcanteen.model;

import java.time.LocalDateTime;

/**
 * The (demo) payment of an order. No real money is involved.
 */
public class Payment {

    public static final String METHOD_UPI = "UPI";
    public static final String METHOD_CARD = "CARD";
    public static final String METHOD_CASH = "CASH";

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_REFUNDED = "REFUNDED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private int paymentId;
    private int orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;

    public Payment() {
    }

    public Payment(int orderId, double amount, String paymentMethod, String paymentStatus,
                   String transactionId, LocalDateTime paymentDate) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
    }

    /** Friendly text for the payment method. */
    public String getMethodLabel() {
        if (METHOD_UPI.equals(paymentMethod)) {
            return "UPI";
        } else if (METHOD_CARD.equals(paymentMethod)) {
            return "Card";
        }
        return "Cash at Counter";
    }

    public boolean isPaid() {
        return STATUS_SUCCESS.equals(paymentStatus);
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
