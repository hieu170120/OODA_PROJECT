package com.foodorder.entity;

import com.foodorder.specification.CompositeEligibilityRule;
import com.foodorder.specification.DateRangeRule;
import com.foodorder.specification.MinOrderRule;
import com.foodorder.strategy.coupon.FixedAmountDiscountStrategy;
import com.foodorder.strategy.coupon.PercentageDiscountStrategy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.foodorder.specification.EligibilityRule;
import com.foodorder.strategy.coupon.DiscountStrategy;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")

public class Coupon {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "max_discount")
    private Double maxDiscount;

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

        double subtotal = order.calculateSubTotal();
        if (isPercentageCoupon()) {
            double discount = subtotal * (discountValue / 100.0);
            if (maxDiscount != null && maxDiscount > 0 && discount > maxDiscount) {
                discount = maxDiscount;
            }
            return Math.max(discount, 0.0);
        }

        return Math.min(discountValue, subtotal);
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return false;
        }
        return true;
    }

    public String getDiscountDescription() {
        if (isPercentageCoupon()) {
            String cap = maxDiscount != null && maxDiscount > 0 ? String.format(" (toi da %,.0fđ)", maxDiscount) : "";
            return String.format("%s%%%s", formatMoney(discountValue != null ? discountValue : 0.0), cap);
        }
        return String.format("%sđ", formatMoney(discountValue != null ? discountValue : 0.0));
    }

    public String getCouponSummary() {
        return String.format("%s | Don toi thieu %sđ | %s - %s",
                getDiscountDescription(),
                formatMoney(minOrderValue != null ? minOrderValue : 0.0),
                validFrom != null ? validFrom.toString() : "N/A",
                validUntil != null ? validUntil.toString() : "N/A");
    }

    public void refreshBehavior() {
        EligibilityRule dateRule = new DateRangeRule(validFrom, validUntil);
        EligibilityRule minOrderRule = new MinOrderRule(minOrderValue != null ? minOrderValue : 0.0);
        CompositeEligibilityRule compositeRule = new CompositeEligibilityRule(List.of(dateRule, minOrderRule));
        setEligibilityRule(compositeRule);

        if (isPercentageCoupon()) {
            PercentageDiscountStrategy strategy = new PercentageDiscountStrategy(discountValue != null ? discountValue : 0.0,
                    maxDiscount != null ? maxDiscount : 0.0);
            setDiscountStrategy(strategy);
        } else {
            FixedAmountDiscountStrategy strategy = new FixedAmountDiscountStrategy(discountValue != null ? discountValue : 0.0);
            setDiscountStrategy(strategy);
        }
    }

    public boolean isPercentageCoupon() {
        return maxDiscount != null;
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f", Math.max(amount, 0));
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
