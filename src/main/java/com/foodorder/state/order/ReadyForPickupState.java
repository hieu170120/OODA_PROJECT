package com.foodorder.state.order;

import com.foodorder.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReadyForPickupState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.READY_FOR_PICKUP;
    }

    @Override
    public Set<OrderStatus> getForwardTargets() {
        return Set.of(OrderStatus.FINISHED);
    }
}
