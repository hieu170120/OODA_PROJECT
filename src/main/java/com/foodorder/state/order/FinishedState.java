package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FinishedState extends AbstractOrderState {

    public FinishedState() {
        super(OrderStatus.FINISHED, Set.of());
    }
}
