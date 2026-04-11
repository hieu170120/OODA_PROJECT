package com.foodorder.strategy.payment;

import org.springframework.stereotype.Component;

/**
 * Chuyển khoản ngân hàng (demo) — Strategy cho {@link com.foodorder.model.enums.PaymentMethod#BANKING}.
 */
@Component
public class BankingPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount > 0 && transactionId != null && !transactionId.isEmpty();
    }
}