package com.foodorder.model;

public class Coupon {
    private String couponId;
    private String couponCode;

    public Coupon() {
    }

    public Coupon(String couponId, String couponCode) {
        this.couponId = couponId;
        this.couponCode = couponCode;
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

}
