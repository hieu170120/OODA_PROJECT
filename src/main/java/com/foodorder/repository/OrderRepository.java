package com.foodorder.repository;

import com.foodorder.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    
    // Fake Database
    private final Map<String, Order> database = new HashMap<>();

    public Order save(Order order) {
        database.put(order.getOrderId(), order);
        return order;
    }

    public List<Order> findAll() {
        return new ArrayList<>(database.values());
    }
    
    public Order findById(String orderId) {
        return database.get(orderId);
    }
}
