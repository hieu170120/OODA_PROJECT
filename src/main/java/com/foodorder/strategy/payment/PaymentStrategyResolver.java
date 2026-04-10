package com.foodorder.strategy.payment;

import com.foodorder.model.enums.PaymentMethod;
import org.springframework.stereotype.Component;

/**
 * Ánh xạ {@link PaymentMethod} → bean {@link PaymentStrategy} (Strategy Pattern + DI).
 */
@Component
public class PaymentStrategyResolver {

    private final CodPaymentStrategy codPaymentStrategy;
    private final BankingPaymentStrategy bankingPaymentStrategy;

    public PaymentStrategyResolver(CodPaymentStrategy codPaymentStrategy,
                                   BankingPaymentStrategy bankingPaymentStrategy) {
        this.codPaymentStrategy = codPaymentStrategy;
        this.bankingPaymentStrategy = bankingPaymentStrategy;
    }

    public PaymentStrategy resolve(PaymentMethod method) {
        if (method == null) {
            return null;
        }
        return switch (method) {
            case COD -> codPaymentStrategy;
            case BANKING -> bankingPaymentStrategy;
        };
    }
}