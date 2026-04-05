package com.foodorder.strategy;

public class BankingPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount > 0 && transactionId != null && !transactionId.isEmpty();
    }
}
