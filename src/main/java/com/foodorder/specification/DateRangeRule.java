package com.foodorder.specification;

import com.foodorder.model.Order;

import java.time.LocalDateTime;

public class DateRangeRule implements EligibilityRule {
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    public DateRangeRule() {
    }

    public DateRangeRule(LocalDateTime validFrom, LocalDateTime validUntil) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    @Override
    public boolean isSatisfiedBy(Order order) {
        if (order == null || order.getOrderTime() == null) {
            return false;
        }

        LocalDateTime orderTime = order.getOrderTime();
        if (validFrom != null && orderTime.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && orderTime.isAfter(validUntil)) {
            return false;
        }
        return true;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
