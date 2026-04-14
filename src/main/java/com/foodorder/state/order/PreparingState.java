package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PreparingState extends AbstractOrderState {

    public PreparingState() {
        super(OrderStatus.PREPARING,
                Set.of(OrderStatus.DELIVERY, OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED));
    }
}
