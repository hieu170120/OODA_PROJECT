package com.foodorder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.foodorder.specification.EligibilityRule;
import com.foodorder.strategy.coupon.DiscountStrategy;



import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")



public class Coupon {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "min_order_value", nullable = false)
    private Double minOrderValue;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Transient
    private EligibilityRule eligibilityRule;

    @Transient
    private DiscountStrategy discountStrategy;

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

        return order.calculateSubTotal() >= (minOrderValue != null ? minOrderValue : 0.0);
    }

    public String getCouponCode() {
        return id;
    }

    public double calculateDiscount(Order order) {
        if (!isValid(order) || discountValue == null) {
            return 0.0;
        }
        
        if (discountStrategy != null) {
            return discountStrategy.calculateDiscount(order);
        }

        return Math.min(discountValue, order.calculateSubTotal());
    }

    public Coupon() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
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
