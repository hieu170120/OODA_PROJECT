package com.foodorder.strategy.coupon;

import com.foodorder.entity.Order;

public interface DiscountStrategy {
    double calculateDiscount(Order order);
}
