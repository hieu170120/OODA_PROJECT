package com.foodorder.dto;

import com.foodorder.entity.CouponEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CouponRequestDTO {
    private String couponCode;
    private double discountValue;
    private double minOrderValue;
    private boolean percentage;
    private Double maxDiscount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime validFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime validUntil;

    private boolean active;
    private Integer usageLimit;

    public CouponEntity toEntity() {
        CouponEntity entity = new CouponEntity();
        entity.setCouponCode(couponCode);
        entity.setDiscountValue(discountValue);
        entity.setMinOrderValue(minOrderValue);
        entity.setPercentage(percentage);
        entity.setMaxDiscount(percentage ? maxDiscount : null);
        entity.setValidFrom(validFrom);
        entity.setValidUntil(validUntil);
        entity.setActive(active);
        entity.setUsageLimit(usageLimit);
        return entity;
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

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }
}
