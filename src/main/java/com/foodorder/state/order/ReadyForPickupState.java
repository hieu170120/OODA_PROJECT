package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReadyForPickupState extends AbstractOrderState {

    public ReadyForPickupState() {
        super(OrderStatus.READY_FOR_PICKUP, Set.of(OrderStatus.FINISHED));
    }
}
