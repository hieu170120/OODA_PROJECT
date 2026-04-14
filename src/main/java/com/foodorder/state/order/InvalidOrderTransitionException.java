package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;

public class InvalidOrderTransitionException extends IllegalStateException {

    public InvalidOrderTransitionException(OrderStatus from, OrderStatus to) {
        super("Không thể chuyển đơn từ " + from + " sang " + to);
    }
}
