package com.foodorder.strategy.coupon;

import com.foodorder.model.Order;

public interface DiscountStrategy {
    double calculateDiscount(Order order);
}
