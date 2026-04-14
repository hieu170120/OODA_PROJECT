package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CancelledState extends AbstractOrderState {

    public CancelledState() {
        super(OrderStatus.CANCELLED, Set.of());
    }
}
