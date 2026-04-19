package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeliveryState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERY;
    }

    @Override
    public Set<OrderStatus> getForwardTargets() {
        return Set.of(OrderStatus.FINISHED);
    }
}
