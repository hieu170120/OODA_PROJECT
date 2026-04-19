package com.foodorder.state.order;

import com.foodorder.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FinishedState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.FINISHED;
    }

    @Override
    public Set<OrderStatus> getForwardTargets() {
        return Set.of();
    }
}
