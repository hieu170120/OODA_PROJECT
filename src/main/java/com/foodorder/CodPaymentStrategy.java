package com.foodorder;

public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount >= 0 && transactionId != null;
    }
}
