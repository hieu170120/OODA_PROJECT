package com.foodorder.strategy.payment;

import org.springframework.stereotype.Component;

/**
 * Thanh toán khi nhận hàng — mã {@code COD}.
 */
@Component
public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public String getMethodCode() {
        return "COD";
    }

    @Override
    public boolean isDeferredPayment() {
        return true;
    }

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount >= 0 && transactionId != null;
    }
}