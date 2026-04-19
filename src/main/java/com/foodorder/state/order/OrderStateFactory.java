package com.foodorder.state.order;

import com.foodorder.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class OrderStateFactory {

    private final Map<OrderStatus, OrderState> byStatus;

    @Autowired
    public OrderStateFactory(List<OrderState> states) {
        EnumMap<OrderStatus, OrderState> map = new EnumMap<>(OrderStatus.class);
        for (OrderState state : states) {
            if (map.put(state.getStatus(), state) != null) {
                throw new IllegalStateException("Trùng OrderState cho: " + state.getStatus());
            }
        }
        for (OrderStatus os : OrderStatus.values()) {
            if (!map.containsKey(os)) {
                throw new IllegalStateException("Thiếu OrderState cho: " + os);
            }
        }
        this.byStatus = Collections.unmodifiableMap(map);
    }

    public OrderState forStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Trạng thái đơn không được null.");
        }
        OrderState s = byStatus.get(status);
        if (s == null) {
            throw new IllegalArgumentException("Không có state cho: " + status);
        }
        return s;
    }

    public Map<OrderStatus, Set<OrderStatus>> selectableGrouped() {
        EnumMap<OrderStatus, Set<OrderStatus>> out = new EnumMap<>(OrderStatus.class);
        for (OrderStatus os : OrderStatus.values()) {
            out.put(os, forStatus(os).selectableStatuses());
        }
        return Collections.unmodifiableMap(out);
    }
}
