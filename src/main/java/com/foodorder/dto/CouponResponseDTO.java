package com.foodorder.dto;

import com.foodorder.entity.CouponEntity;

import java.time.LocalDateTime;

public class CouponResponseDTO {
    private String couponId;
    private String couponCode;
    private double discountValue;
    private double minOrderValue;
    private boolean percentage;
    private Double maxDiscount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean active;
    private Integer usageLimit;
    private Integer usedCount;

    public static CouponResponseDTO fromEntity(CouponEntity entity) {
        if (entity == null) {
            return null;
        }

        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setCouponId(entity.getCouponId());
        dto.setCouponCode(entity.getCouponCode());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setMinOrderValue(entity.getMinOrderValue());
        dto.setPercentage(entity.isPercentage());
        dto.setMaxDiscount(entity.getMaxDiscount());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidUntil(entity.getValidUntil());
        dto.setActive(entity.isActive());
        dto.setUsageLimit(entity.getUsageLimit());
        dto.setUsedCount(entity.getUsedCount());
        return dto;
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

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }
}
