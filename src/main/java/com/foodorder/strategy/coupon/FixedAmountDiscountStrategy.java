package com.foodorder.strategy.coupon;

import com.foodorder.entity.Order;

public class FixedAmountDiscountStrategy implements DiscountStrategy {
    private double amount;

    public FixedAmountDiscountStrategy() {
    }

    public FixedAmountDiscountStrategy(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculateDiscount(Order order) {
        if (order == null || amount <= 0) {
            return 0;
        }
        return amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
