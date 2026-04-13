package com.foodorder.state.order;

import com.foodorder.model.enums.OrderStatus;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractOrderState implements OrderState {

    private final OrderStatus status;
    private final Set<OrderStatus> forwardTargets;

    protected AbstractOrderState(OrderStatus status, Set<OrderStatus> forwardTargets) {
        this.status = status;
        this.forwardTargets = Set.copyOf(forwardTargets);
    }

    @Override
    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public void validateTransition(OrderStatus target) {
        if (target == null) {
            throw new IllegalArgumentException("Trạng thái đích không được null.");
        }
        if (target == status) {
            return;
        }
        if (!forwardTargets.contains(target)) {
            throw new InvalidOrderTransitionException(status, target);
        }
    }

    @Override
    public Set<OrderStatus> selectableStatuses() {
        LinkedHashSet<OrderStatus> out = new LinkedHashSet<>();
        out.add(status);
        out.addAll(forwardTargets);
        return out;
    }
}
