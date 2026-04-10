package com.foodorder.specification;

import com.foodorder.model.Order;

public class MinOrderRule implements EligibilityRule {
    private double minOrderValue;

    public MinOrderRule() {
    }

    public MinOrderRule(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    @Override
    public boolean isSatisfiedBy(Order order) {
        if (order == null) {
            return false;
        }

        return order.calculateSubtotalAmount() >= minOrderValue;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }
}
