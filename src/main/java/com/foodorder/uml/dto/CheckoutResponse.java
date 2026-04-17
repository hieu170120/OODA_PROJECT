package com.foodorder.uml.dto;

import com.foodorder.uml.entity.Address;
import com.foodorder.uml.entity.Coupon;
import com.foodorder.uml.entity.Order;
import com.foodorder.uml.entity.OrderItem;
import com.foodorder.uml.entity.Payment;

import java.util.List;

public class CheckoutResponse {
    private final Order order;
    private final List<OrderItem> orderItems;
    private final Address deliveryAddress;
    private final Coupon coupon;
    private final Payment payment;
    private final double subtotal;
    private final double total;

    public CheckoutResponse(Order order,
                            List<OrderItem> orderItems,
                            Address deliveryAddress,
                            Coupon coupon,
                            Payment payment,
                            double subtotal,
                            double total) {
        this.order = order;
        this.orderItems = orderItems;
        this.deliveryAddress = deliveryAddress;
        this.coupon = coupon;
        this.payment = payment;
        this.subtotal = subtotal;
        this.total = total;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Payment getPayment() {
        return payment;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }
}
