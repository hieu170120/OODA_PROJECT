package com.foodorder.service;

import com.foodorder.entity.Coupon;
import com.foodorder.entity.Order;
import com.foodorder.enums.DiscountType;
import com.foodorder.repository.CouponRepository;
import com.foodorder.specification.CompositeEligibilityRule;
import com.foodorder.specification.DateRangeRule;
import com.foodorder.specification.MinOrderRule;
import com.foodorder.strategy.coupon.FixedAmountDiscountStrategy;
import com.foodorder.strategy.coupon.PercentageDiscountStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

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

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::prepareCoupon)
                .toList();
    }

    public Coupon addCoupon(Coupon coupon) {
        Coupon preparedCoupon = prepareCoupon(coupon);
        if (preparedCoupon.getId() == null || preparedCoupon.getId().isBlank()) {
            throw new IllegalArgumentException("Ma coupon khong duoc de trong.");
        }
        if (couponRepository.existsById(preparedCoupon.getId())) {
            throw new IllegalArgumentException("Ma coupon da ton tai.");
        }
        return couponRepository.save(preparedCoupon);
    }

    public Coupon updateCoupon(String couponCode, Coupon updatedCoupon) {
        String normalizedCode = normalizeCouponCode(couponCode);
        Coupon existing = couponRepository.findById(normalizedCode)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay coupon: " + normalizedCode));

        Coupon preparedCoupon = prepareCoupon(updatedCoupon);
        preparedCoupon.setId(existing.getId());
        return couponRepository.save(preparedCoupon);
    }

    public void deleteCoupon(String couponCode) {
        String normalizedCode = normalizeCouponCode(couponCode);
        if (!couponRepository.existsById(normalizedCode)) {
            throw new IllegalArgumentException("Khong tim thay coupon: " + normalizedCode);
        }
        couponRepository.deleteById(normalizedCode);
    }

    private Coupon getCouponByCode(String code) {
        return couponRepository.findById(normalizeCouponCode(code))
                .map(this::prepareCoupon)
                .orElse(null);
    }

    private List<Coupon> getCouponCatalog() {
        return couponRepository.findAll().stream()
            .map(this::prepareCoupon)
            .toList();
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
        String discountText = coupon.getDiscountType() == DiscountType.PERCENTAGE
            ? formatMoney(coupon.getDiscountValue() != null ? coupon.getDiscountValue() : 0.0) + "%"
            : formatMoney(coupon.getDiscountValue() != null ? coupon.getDiscountValue() : 0.0) + "đ";
        String capText = coupon.getDiscountType() == DiscountType.PERCENTAGE
            && coupon.getMaxDiscount() != null
            && coupon.getMaxDiscount() > 0
            ? String.format(" toi da %sđ", formatMoney(coupon.getMaxDiscount()))
            : "";
        return String.format("Giam %s%s (don tu %sđ)",
            discountText,
            capText,
            formatMoney(coupon.getMinOrderValue() != null ? coupon.getMinOrderValue() : 0.0));
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f", Math.max(amount, 0));
    }

    private Coupon prepareCoupon(Coupon coupon) {
        if (coupon == null) {
            return null;
        }

        if (coupon.getId() != null) {
            coupon.setId(normalizeCouponCode(coupon.getId()));
        }

        if (coupon.getDiscountType() == null) {
            coupon.setDiscountType(DiscountType.FIXED_AMOUNT);
        }

        if (coupon.getMinOrderValue() == null) {
            coupon.setMinOrderValue(0.0);
        }

        coupon.setEligibilityRule(new CompositeEligibilityRule(List.of(
                new DateRangeRule(coupon.getValidFrom(), coupon.getValidUntil()),
                new MinOrderRule(coupon.getMinOrderValue())
        )));

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            coupon.setDiscountStrategy(new PercentageDiscountStrategy(
                    coupon.getDiscountValue() != null ? coupon.getDiscountValue() : 0.0,
                    coupon.getMaxDiscount() != null ? coupon.getMaxDiscount() : 0.0
            ));
        } else {
            coupon.setDiscountStrategy(new FixedAmountDiscountStrategy(
                    coupon.getDiscountValue() != null ? coupon.getDiscountValue() : 0.0
            ));
        }

        return coupon;
    }

    private String normalizeCouponCode(String code) {
        if (code == null) {
            return null;
        }
        return code.trim().toUpperCase();
    }
}
