package com.foodorder.specification;

import com.foodorder.entity.Order;

public interface EligibilityRule {
    boolean isSatisfiedBy(Order order);
}
