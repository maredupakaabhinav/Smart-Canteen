package com.smartcanteen.service;

/**
 * INTERFACE (abstraction): says WHAT a payment service must do, not HOW.
 * OrderService only talks to this interface, so it does not care whether the payment is
 * a demo online payment or cash at the counter (POLYMORPHISM).
 */
public interface PaymentService {

    /** Tries to take the payment. Returns true if it worked. */
    boolean processPayment(double amount);

    /** UPI, CARD or CASH - the value saved in the payments table. */
    String getPaymentMethod();

    /** Status saved after a successful call: SUCCESS (paid) or PENDING (pay later). */
    String getStatusAfterPayment();

    /** A reference number for this payment, e.g. DEMO123456. */
    String createTransactionId();
}
