package com.foodorder.model;

import com.foodorder.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private Customer customer;
    private LocalDateTime orderTime;
    private double subTotal;
    private double shippingFee;
    private String shippingAddress;
    private LocalDateTime estimatedPickupTime;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private Coupon coupon;
    private Payment payment;
    private List<Notification> notifications;

    public Order() {
        this.orderItems = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public Order(String orderId, Customer customer, LocalDateTime orderTime, double subTotal, double shippingFee, String shippingAddress, LocalDateTime estimatedPickupTime, OrderStatus status) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderTime = orderTime;
        this.subTotal = subTotal;
        this.shippingFee = shippingFee;
        this.shippingAddress = shippingAddress;
        this.estimatedPickupTime = estimatedPickupTime;
        this.status = status;
        this.orderItems = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public double calculateTotal() {
        double total = 0.0;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                if (item != null) {
                    total += item.calculateSubTotal();
                }
            }
        }
        total += shippingFee;
        
        if (coupon != null && coupon.isValid(this)) {
            if (coupon.isPercentage()) {
                total -= total * (coupon.getDiscountValue() / 100.0);
            } else {
                total -= coupon.getDiscountValue();
            }
            if (total < 0) {
                total = 0;
            }
        }
        return total;
    }
    
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }
    
    public void applyCoupon(Coupon coupon) {
        if (coupon != null && coupon.isValid(this)) {
            this.coupon = coupon;
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getEstimatedPickupTime() {
        return estimatedPickupTime;
    }

    public void setEstimatedPickupTime(LocalDateTime estimatedPickupTime) {
        this.estimatedPickupTime = estimatedPickupTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
