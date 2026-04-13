package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeliveryState extends AbstractOrderState {

    public DeliveryState() {
        super(OrderStatus.DELIVERY, Set.of(OrderStatus.FINISHED));
    }
}
