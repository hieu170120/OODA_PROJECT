package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReceivedState extends AbstractOrderState {

    public ReceivedState() {
        super(OrderStatus.RECEIVED, Set.of(OrderStatus.PREPARING, OrderStatus.CANCELLED));
    }
}
