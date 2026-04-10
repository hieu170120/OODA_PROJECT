package com.foodorder.model;

import java.time.LocalDateTime;

public class Coupon {
    private String couponId;
    private String couponCode;
    private double discountValue;
    private double minOrderValue;
    private boolean percentage;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

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
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(validFrom) || now.isAfter(validUntil)) {
            return false;
        }
        
        if (order != null && order.getSubTotal() < minOrderValue) {
            return false;
        }
        
        return true;
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
}
