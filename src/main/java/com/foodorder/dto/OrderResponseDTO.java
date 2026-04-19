package com.foodorder.dto;

import com.foodorder.entity.Order;
import com.foodorder.entity.Payment;
import com.foodorder.enums.OrderStatus;
import com.foodorder.enums.PaymentMethod;
import com.foodorder.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDTO {
    private String orderId;
    private String customerName;
    private LocalDateTime orderTime;
    private double subTotal;
    private double shippingFee;
    private double totalAmount;
    private String shippingAddress;
    private LocalDateTime estimatedPickupTime;
    private OrderStatus status;
    /** Mã phương thức (COD, BANKING, …) khớp PaymentStrategy. */
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
    private String paymentId;
    private List<CartItemDTO> orderItems;

    public OrderResponseDTO() {
    }

    public static OrderResponseDTO fromEntity(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getFullName());
        }
        dto.setOrderTime(order.getOrderTime());
        dto.setSubTotal(order.calculateSubTotal());
        dto.setShippingFee(0);
        dto.setTotalAmount(order.calculateTotal());
        dto.setShippingAddress(null);
        dto.setEstimatedPickupTime(null);
        dto.setStatus(order.getStatus());

        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                    .map(CartItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        Payment payment = order.getPayment();
        if (payment != null) {
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setPaymentStatus(payment.getPaymentStatus());
            dto.setPaidAt(payment.getPaidAt());
            dto.setPaymentId(payment.getPaymentId());
        }

        return dto;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
    
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public List<CartItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
