package com.foodorder.strategy.payment;

import com.foodorder.model.enums.PaymentMethod;
import org.springframework.stereotype.Component;

/**
 * Chuyển khoản ngân hàng (demo) — mã {@code BANKING}.
 */
@Component
public class BankingPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getMethodCode() {
        return PaymentMethod.BANKING;
    }

    @Override
    public boolean processPayment(double amount, String transactionId) {
        return amount > 0 && transactionId != null && !transactionId.isEmpty();
    }
}