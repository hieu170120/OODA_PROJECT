package com.foodorder.strategy.payment;

import org.springframework.stereotype.Component;

/**
 * Thanh toán khi nhận hàng — Strategy cụ thể cho {@link com.foodorder.model.enums.PaymentMethod#COD}.
 */
@Component
public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount >= 0 && transactionId != null;
    }
}