package com.foodorder;

import java.time.LocalDateTime;

public class Coupon {
    private String couponId;
    private String code;
    private double discountValue;
    private boolean percentage;
    private LocalDateTime validUntil;

    public Coupon() {
    }

    public Coupon(String couponId, String code, double discountValue, boolean percentage, LocalDateTime validUntil) {
        this.couponId = couponId;
        this.code = code;
        this.discountValue = discountValue;
        this.percentage = percentage;
        this.validUntil = validUntil;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
