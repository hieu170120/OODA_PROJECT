package com.foodorder.strategy.payment;

public interface PaymentStrategy {

    boolean processPayment(double amount, String transactionId);
}