package com.smartcanteen.service;

import com.smartcanteen.model.Payment;

import java.util.Random;

/**
 * Cash at the counter: nothing is charged now, the payment stays PENDING
 * until the student collects the order and pays at the canteen counter.
 */
public class CashPaymentService implements PaymentService {

    private final Random random = new Random();

    @Override
    public boolean processPayment(double amount) {
        return amount > 0;   // nothing to charge online
    }

    @Override
    public String getPaymentMethod() {
        return Payment.METHOD_CASH;
    }

    @Override
    public String getStatusAfterPayment() {
        return Payment.STATUS_PENDING;
    }

    @Override
    public String createTransactionId() {
        return "CASH" + (100000 + random.nextInt(900000));
    }
}
