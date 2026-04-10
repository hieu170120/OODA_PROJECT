package com.foodorder.specification;

import com.foodorder.model.Order;

public interface EligibilityRule {
    boolean isSatisfiedBy(Order order);
}
