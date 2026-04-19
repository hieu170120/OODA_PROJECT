package com.foodorder.service;

import com.foodorder.entity.Coupon;
import com.foodorder.entity.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    public static class CouponOption {
        private final String code;
        private final String description;
        private final boolean eligible;
        private final double estimatedDiscount;
        private final String reason;

        public CouponOption(String code, String description, boolean eligible, double estimatedDiscount, String reason) {
            this.code = code;
            this.description = description;
            this.eligible = eligible;
            this.estimatedDiscount = estimatedDiscount;
            this.reason = reason;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public boolean isEligible() {
            return eligible;
        }

        public double getEstimatedDiscount() {
            return estimatedDiscount;
        }

        public String getReason() {
            return reason;
        }
    }

    public Coupon resolveCoupon(String rawCode, Order previewOrder) {
        if (rawCode == null || rawCode.isBlank()) {
            return null;
        }

        Coupon coupon = getCouponByCode(rawCode.trim().toUpperCase());
        if (coupon == null) {
            throw new IllegalArgumentException("Ma coupon khong ton tai.");
        }
        if (!coupon.isValid(previewOrder)) {
            throw new IllegalArgumentException("Ma coupon chua du dieu kien ap dung.");
        }
        return coupon;
    }

    public List<CouponOption> getCouponOptions(Order previewOrder) {
        List<CouponOption> options = new ArrayList<>();
        for (Coupon coupon : getCouponCatalog()) {
            options.add(buildOption(coupon, previewOrder));
        }
        return options;
    }

    private Coupon getCouponByCode(String code) {
        for (Coupon coupon : getCouponCatalog()) {
            if (coupon.getCouponCode() != null && coupon.getCouponCode().equalsIgnoreCase(code)) {
                return coupon;
            }
        }
        return null;
    }

    private List<Coupon> getCouponCatalog() {
        LocalDateTime now = LocalDateTime.now();
        return List.of(
                buildCoupon("SAVE10", 10000.0, 50000.0, now.minusDays(30), now.plusMonths(6)),
                buildCoupon("FREESHIP15", 15000.0, 30000.0, now.minusDays(30), now.plusMonths(6)),
                buildCoupon("BIGSALE50", 50000.0, 200000.0, now.minusDays(7), now.plusDays(30))
        );
    }

    private CouponOption buildOption(Coupon coupon, Order previewOrder) {
        String description = buildDescription(coupon);
        if (previewOrder == null) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0.0,
                    "Chua co du lieu don hang de kiem tra dieu kien.");
        }

        if (!coupon.isValid(previewOrder)) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0.0,
                    "Coupon chua du dieu kien ap dung.");
        }

        return new CouponOption(coupon.getCouponCode(), description, true, coupon.calculateDiscount(previewOrder),
                "Du dieu kien ap dung.");
    }

    private String buildDescription(Coupon coupon) {
        return String.format("Giam %sđ (don tu %sđ)",
                formatMoney(coupon.getDiscountValue() != null ? coupon.getDiscountValue() : 0.0),
                formatMoney(coupon.getMinOrderValue() != null ? coupon.getMinOrderValue() : 0.0));
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f", Math.max(amount, 0));
    }

    private Coupon buildCoupon(String id, double discountValue, double minOrderValue,
                               LocalDateTime validFrom, LocalDateTime validUntil) {
        Coupon coupon = new Coupon();
        coupon.setId(id);
        coupon.setDiscountValue(discountValue);
        coupon.setMinOrderValue(minOrderValue);
        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);
        return coupon;
    }
}
