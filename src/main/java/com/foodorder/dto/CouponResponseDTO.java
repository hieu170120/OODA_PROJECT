package com.foodorder.dto;

import com.foodorder.entity.Coupon;
import com.foodorder.enums.DiscountType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CouponResponseDTO {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private String couponCode;
    private DiscountType discountType;
    private String discountTypeName;
    private Double discountValue;
    private Double maxDiscount;
    private Double minOrderValue;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String validFromText;
    private String validUntilText;
    private String discountDisplay;
    private String description;
    private boolean active;

    public CouponResponseDTO() {
    }

    public static CouponResponseDTO fromEntity(Coupon coupon) {
        if (coupon == null) {
            return null;
        }

        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setCouponCode(coupon.getCouponCode());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setDiscountTypeName(coupon.getDiscountType() != null ? coupon.getDiscountType().name() : "FIXED_AMOUNT");
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMaxDiscount(coupon.getMaxDiscount());
        dto.setMinOrderValue(coupon.getMinOrderValue());
        dto.setValidFrom(coupon.getValidFrom());
        dto.setValidUntil(coupon.getValidUntil());
        dto.setValidFromText(coupon.getValidFrom() != null ? coupon.getValidFrom().format(INPUT_FORMAT) : "");
        dto.setValidUntilText(coupon.getValidUntil() != null ? coupon.getValidUntil().format(INPUT_FORMAT) : "");
        dto.setDiscountDisplay(coupon.getDiscountDescription());
        dto.setDescription(coupon.getCouponSummary());
        dto.setActive(coupon.isActive());
        return dto;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
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

    public String getValidFromText() {
        return validFromText;
    }

    public void setValidFromText(String validFromText) {
        this.validFromText = validFromText;
    }

    public String getValidUntilText() {
        return validUntilText;
    }

    public void setValidUntilText(String validUntilText) {
        this.validUntilText = validUntilText;
    }

    public String getDiscountDisplay() {
        return discountDisplay;
    }

    public void setDiscountDisplay(String discountDisplay) {
        this.discountDisplay = discountDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}