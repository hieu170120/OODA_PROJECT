package com.foodorder.strategy.payment;

import org.springframework.stereotype.Component;

/**
 * Chuyển khoản ngân hàng (demo) — mã {@code BANKING}.
 */
@Component
public class BankingPaymentStrategy implements PaymentStrategy {

    @Override
    public String getMethodCode() {
        return "BANKING";
    }

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount > 0 && transactionId != null && !transactionId.isEmpty();
    }
}