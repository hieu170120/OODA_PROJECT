package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;

import java.util.LinkedHashSet;
import java.util.Set;

public interface OrderState {

    OrderStatus getStatus();

    Set<OrderStatus> getForwardTargets();

    default void validateTransition(OrderStatus target) {
        if (target == null) {
            throw new IllegalArgumentException("Trạng thái đích không được null.");
        }
        if (target == getStatus()) {
            return;
        }
        if (!getForwardTargets().contains(target)) {
            throw new InvalidOrderTransitionException(getStatus(), target);
        }
    }

    default Set<OrderStatus> selectableStatuses() {
        LinkedHashSet<OrderStatus> out = new LinkedHashSet<>();
        out.add(getStatus());
        out.addAll(getForwardTargets());
        return out;
    }
}
