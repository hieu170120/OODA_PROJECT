package com.foodorder;

public interface PaymentStrategy {

    boolean processPayment(double amount, String transactionId);
}
