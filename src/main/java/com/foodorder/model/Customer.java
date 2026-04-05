package com.foodorder.model;

import com.foodorder.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private int rewardPoints;
    private ShoppingCart shoppingCart;
    private List<Order> orders;

    public Customer() {
        this.orders = new ArrayList<>();
    }

    public Customer(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt, int rewardPoints) {
        super(userId, fullName, email, password, phone, createdAt);
        this.rewardPoints = rewardPoints;
        this.orders = new ArrayList<>();
    }

    public Customer(String userId, String fullName, String email, String password, String phone, LocalDateTime createdAt, int rewardPoints, ShoppingCart shoppingCart) {
        super(userId, fullName, email, password, phone, createdAt);
        this.rewardPoints = rewardPoints;
        this.shoppingCart = shoppingCart;
        this.orders = new ArrayList<>();
    }

    public OrderStatus trackOrderStatus(String orderId) {
        if (orders == null) {
            return null;
        }
        for (Order order : orders) {
            if (order != null && orderId != null && orderId.equals(order.getOrderId())) {
                return order.getStatus(); // Đã sửa lỗi: getOrderStatus() -> getStatus()
            }
        }
        return null;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
