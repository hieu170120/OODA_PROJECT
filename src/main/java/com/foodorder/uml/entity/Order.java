package com.foodorder.uml.entity;

import com.foodorder.uml.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "uml_orders")
public class Order {
    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(String id, LocalDateTime orderTime, OrderStatus orderStatus) {
        this.id = id;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
    }

    public void applyCoupon(Coupon coupon) {
        if (coupon == null || !coupon.isValid(this)) {
            throw new IllegalArgumentException("Coupon is not valid for this order.");
        }
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    public double calculateSubtotal() {
        throw new UnsupportedOperationException("Subtotal requires order items managed outside the entity.");
    }

    public double calculateTotal() {
        throw new UnsupportedOperationException("Total requires order items and coupon logic managed outside the entity.");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
