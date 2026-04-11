package com.foodorder.model;

import com.foodorder.specification.EligibilityRule;
import com.foodorder.strategy.coupon.DiscountStrategy;

import java.time.LocalDateTime;

public class Coupon {
    private String couponId;
    private String couponCode;
    private double discountValue;
    private double minOrderValue;
    private boolean percentage;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private DiscountStrategy discountStrategy;
    private EligibilityRule eligibilityRule;

    public Coupon() {
    }

    public Coupon(String couponId, String couponCode, double discountValue, double minOrderValue, boolean percentage, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.percentage = percentage;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public boolean isValid(Order order) {
        if (order == null) {
            return false;
        }

        if (eligibilityRule != null) {
            return eligibilityRule.isSatisfiedBy(order);
        }

        LocalDateTime now = LocalDateTime.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return false;
        }

        if (order.calculateSubtotalAmount() < minOrderValue) {
            return false;
        }

        return true;
    }

    public double calculateDiscount(Order order) {
        if (!isValid(order)) {
            return 0;
        }

        double discount;
        if (discountStrategy != null) {
            discount = discountStrategy.calculateDiscount(order);
        } else if (percentage) {
            discount = order.calculateSubtotalAmount() * (discountValue / 100.0);
        } else {
            discount = discountValue;
        }

        if (discount < 0) {
            return 0;
        }

        return Math.min(discount, order.calculateSubtotalAmount());
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
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

    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public EligibilityRule getEligibilityRule() {
        return eligibilityRule;
    }

    public void setEligibilityRule(EligibilityRule eligibilityRule) {
        this.eligibilityRule = eligibilityRule;
    }
}
