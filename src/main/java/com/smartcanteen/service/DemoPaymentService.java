package com.smartcanteen.service;

import com.smartcanteen.model.Payment;

import java.util.Random;

/**
 * A SIMULATED online payment (used for UPI and Card). No real payment gateway is used
 * and no money moves - it always succeeds for a valid amount.
 */
public class DemoPaymentService implements PaymentService {

    private final String paymentMethod;
    private final Random random = new Random();

    public DemoPaymentService(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean processPayment(double amount) {
        return amount > 0;
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String getStatusAfterPayment() {
        return Payment.STATUS_SUCCESS;
    }

    @Override
    public String createTransactionId() {
        return "DEMO" + (100000 + random.nextInt(900000));
    }
}
