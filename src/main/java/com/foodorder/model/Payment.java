package com.foodorder.model;

import com.foodorder.model.enums.PaymentStatus;
import com.foodorder.strategy.payment.PaymentStrategy;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private Order order;
    private double amount;
    /** Mã phương thức (chuỗi), khớp {@link com.foodorder.strategy.PaymentStrategy#getMethodCode()}. */
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
    private PaymentStrategy paymentStrategy;

    public Payment() {
    }

    public Payment(String paymentId, Order order, double amount, String paymentMethod, PaymentStatus paymentStatus, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
    }

    public Payment(String paymentId, Order order, double amount, String paymentMethod, PaymentStatus paymentStatus, LocalDateTime paidAt, PaymentStrategy paymentStrategy) {
        this.paymentId = paymentId;
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processTransaction() {
        if (order == null || amount < 0) {
            this.paymentStatus = PaymentStatus.FAILED;
            return false;
        }
        if (this.paymentStrategy == null) {
            this.paymentStatus = PaymentStatus.FAILED;
            return false;
        }
        String transactionId = this.paymentId != null ? this.paymentId : "";
        boolean success = this.paymentStrategy.processPayment(this.amount, transactionId);
        if (success) {
            this.paymentStatus = PaymentStatus.COMPLETED;
            this.paidAt = LocalDateTime.now();
        } else {
            this.paymentStatus = PaymentStatus.FAILED;
        }
        return success;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
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

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}
