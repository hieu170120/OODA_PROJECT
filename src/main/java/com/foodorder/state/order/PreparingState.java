package com.foodorder.state.order;

import com.foodorder.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PreparingState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PREPARING;
    }

    @Override
    public Set<OrderStatus> getForwardTargets() {
        return Set.of(OrderStatus.DELIVERY, OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED);
    }
}
