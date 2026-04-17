package com.foodorder.uml.service;

import com.foodorder.uml.dto.CheckoutRequest;
import com.foodorder.uml.dto.CheckoutResponse;
import com.foodorder.uml.entity.Address;
import com.foodorder.uml.entity.Coupon;
import com.foodorder.uml.entity.Order;
import com.foodorder.uml.entity.OrderItem;
import com.foodorder.uml.entity.Payment;
import com.foodorder.uml.enums.PaymentStatus;
import com.foodorder.uml.repository.AddressRepository;
import com.foodorder.uml.repository.CouponRepository;
import com.foodorder.uml.repository.OrderItemRepository;
import com.foodorder.uml.repository.OrderRepository;
import com.foodorder.uml.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutApplicationService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;

    public CheckoutApplicationService(OrderRepository orderRepository,
                                      OrderItemRepository orderItemRepository,
                                      PaymentRepository paymentRepository,
                                      CouponRepository couponRepository,
                                      AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.couponRepository = couponRepository;
        this.addressRepository = addressRepository;
    }

    public CheckoutResponse checkout(CheckoutRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + request.getOrderId()));
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + request.getPaymentId()));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found: " + request.getAddressId()));

        List<OrderItem> items = loadOrderItems(request.getOrderItemIds());
        double subtotal = calculateSubtotal(items);

        Coupon coupon = null;
        double total = subtotal;
        if (request.getCouponId() != null && !request.getCouponId().isBlank()) {
            coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + request.getCouponId()));
            order.applyCoupon(coupon);
            if (subtotal >= coupon.getMinOrderValue()) {
                total = Math.max(0, subtotal - coupon.getDiscountValue());
            }
        }

        payment.setAmount(total);
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        paymentRepository.save(payment);
        orderRepository.save(order);

        return new CheckoutResponse(order, items, address, coupon, payment, subtotal, total);
    }

    public Payment processPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        payment.processPayment();
        return paymentRepository.save(payment);
    }

    private List<OrderItem> loadOrderItems(List<String> orderItemIds) {
        List<OrderItem> items = new ArrayList<>();
        for (String orderItemId : orderItemIds) {
            items.add(orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new IllegalArgumentException("Order item not found: " + orderItemId)));
        }
        return items;
    }

    private double calculateSubtotal(List<OrderItem> items) {
        double subtotal = 0;
        for (OrderItem item : items) {
            subtotal += item.calculateLineTotal();
        }
        return subtotal;
    }
}
