package com.foodorder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private Customer customer;
    private double subTotal;
    private OrderStatus orderStatus;
    private LocalDateTime placedAt;
    private List<OrderItem> orderItems;
    private Coupon coupon;
    private Payment payment;
    private List<Notification> notifications;

    public Order() {
        this.orderItems = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public Order(String orderId, Customer customer, double subTotal, OrderStatus orderStatus, LocalDateTime placedAt) {
        this.orderId = orderId;
        this.customer = customer;
        this.subTotal = subTotal;
        this.orderStatus = orderStatus;
        this.placedAt = placedAt;
        this.orderItems = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public double calculateTotal() {
        double total = 0.0;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                if (item == null) {
                    continue;
                }
                double line = item.getUnitPrice() * item.getQuantity();
                if (item.getToppings() != null) {
                    for (Topping t : item.getToppings()) {
                        if (t != null) {
                            line += t.getPrice() * item.getQuantity();
                        }
                    }
                }
                total += line;
            }
        }
        if (coupon != null) {
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

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
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
