package com.foodorder.strategy;

public interface PaymentStrategy {

    boolean processPayment(double amount, String transactionId);
}
