package com.foodorder.uml.service;

import com.foodorder.uml.entity.Order;
import com.foodorder.uml.enums.OrderStatus;
import com.foodorder.uml.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderManagementService {
    private final OrderRepository orderRepository;

    public OrderManagementService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        order.updateStatus(newStatus);
        orderRepository.save(order);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }
}
