package com.foodorder.dto;

import com.foodorder.entity.Coupon;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CouponRequestDTO {
    private String couponCode;
    private String strategyType;
    private Double discountValue;
    private Double maxDiscount;
    private Double minOrderValue;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime validFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime validUntil;

    public CouponRequestDTO() {
    }

    public Coupon toEntity() {
        Coupon coupon = new Coupon();
        coupon.setId(couponCode != null ? couponCode.trim().toUpperCase() : null);

        boolean isPercentage = "PERCENTAGE".equalsIgnoreCase(strategyType);
        coupon.setDiscountValue(discountValue);
        coupon.setMaxDiscount(isPercentage ? (maxDiscount != null ? maxDiscount : 0.0) : null);
        coupon.setMinOrderValue(minOrderValue);
        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);
        return coupon;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(Double minOrderValue) {
        this.minOrderValue = minOrderValue;
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