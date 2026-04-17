package com.foodorder.uml.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "uml_coupons")
public class Coupon {
    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column(nullable = false)
    private double discountValue;

    @Column(nullable = false)
    private double minOrderValue;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    public Coupon() {
    }

    public Coupon(String id, double discountValue, double minOrderValue, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.id = id;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public boolean isValid(Order order) {
        if (order == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(validFrom) && !now.isAfter(validUntil);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
