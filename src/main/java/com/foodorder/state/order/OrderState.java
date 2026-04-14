package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;

import java.util.Set;

public interface OrderState {

    OrderStatus getStatus();

    void validateTransition(OrderStatus target);

    Set<OrderStatus> selectableStatuses();
}
