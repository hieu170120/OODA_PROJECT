package com.foodorder.service;

import com.foodorder.model.Coupon;
import com.foodorder.model.Order;
import com.foodorder.specification.CompositeEligibilityRule;
import com.foodorder.specification.DateRangeRule;
import com.foodorder.specification.MinOrderRule;
import com.foodorder.strategy.coupon.FixedAmountDiscountStrategy;
import com.foodorder.strategy.coupon.PercentageDiscountStrategy;
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

        String normalizedCode = rawCode.trim().toUpperCase();
        Coupon coupon = getCouponByCode(normalizedCode);

        if (coupon == null) {
            throw new IllegalArgumentException("Mã coupon không tồn tại.");
        }
        if (!coupon.isValid(previewOrder)) {
            throw new IllegalArgumentException("Mã coupon chưa đủ điều kiện áp dụng.");
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
                buildPercentageCoupon(
                        "CP-SAVE10",
                        "SAVE10",
                        10,
                        30000,
                        50000,
                        now.minusDays(30),
                        now.plusMonths(6)
                ),
                buildFixedCoupon(
                        "CP-FREESHIP15",
                        "FREESHIP15",
                        15000,
                        30000,
                        now.minusDays(30),
                        now.plusMonths(6)
                ),
                buildFixedCoupon(
                        "CP-BIGSALE50",
                        "BIGSALE50",
                        50000,
                        200000,
                        now.minusDays(7),
                        now.plusDays(30)
                )
        );
    }

    private CouponOption buildOption(Coupon coupon, Order previewOrder) {
        String description = buildDescription(coupon);

        if (previewOrder == null) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Chưa có dữ liệu đơn hàng để kiểm tra điều kiện.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Coupon chưa tới thời gian áp dụng.");
        }
        if (coupon.getValidUntil() != null && now.isAfter(coupon.getValidUntil())) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Coupon đã hết hạn.");
        }

        double subtotal = previewOrder.calculateSubtotalAmount();
        if (subtotal < coupon.getMinOrderValue()) {
            double missing = coupon.getMinOrderValue() - subtotal;
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Cần mua thêm " + formatMoney(missing) + "đ để dùng mã này.");
        }

        double estimated = coupon.calculateDiscount(previewOrder);
        return new CouponOption(coupon.getCouponCode(), description, true, estimated,
                "Đủ điều kiện áp dụng.");
    }

    private String buildDescription(Coupon coupon) {
        if (coupon.isPercentage()) {
            double maxDiscount = 0;
            if (coupon.getDiscountStrategy() instanceof PercentageDiscountStrategy percentageStrategy) {
                maxDiscount = percentageStrategy.getMaxDiscount();
            }
            if (maxDiscount > 0) {
                return String.format("Giảm %.0f%%, tối đa %sđ (đơn từ %sđ)",
                        coupon.getDiscountValue(),
                        formatMoney(maxDiscount),
                        formatMoney(coupon.getMinOrderValue()));
            }
            return String.format("Giảm %.0f%% (đơn từ %sđ)",
                    coupon.getDiscountValue(),
                    formatMoney(coupon.getMinOrderValue()));
        }

        return String.format("Giảm %sđ (đơn từ %sđ)",
                formatMoney(coupon.getDiscountValue()),
                formatMoney(coupon.getMinOrderValue()));
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f", Math.max(amount, 0));
    }

    private Coupon buildPercentageCoupon(String id,
                                         String code,
                                         double percent,
                                         double maxDiscount,
                                         double minOrderValue,
                                         LocalDateTime validFrom,
                                         LocalDateTime validUntil) {
        Coupon coupon = new Coupon(id, code, percent, minOrderValue, true, validFrom, validUntil);
        coupon.setDiscountStrategy(new PercentageDiscountStrategy(percent, maxDiscount));
        coupon.setEligibilityRule(buildEligibilityRule(minOrderValue, validFrom, validUntil));
        return coupon;
    }

    private Coupon buildFixedCoupon(String id,
                                    String code,
                                    double amount,
                                    double minOrderValue,
                                    LocalDateTime validFrom,
                                    LocalDateTime validUntil) {
        Coupon coupon = new Coupon(id, code, amount, minOrderValue, false, validFrom, validUntil);
        coupon.setDiscountStrategy(new FixedAmountDiscountStrategy(amount));
        coupon.setEligibilityRule(buildEligibilityRule(minOrderValue, validFrom, validUntil));
        return coupon;
    }

    private CompositeEligibilityRule buildEligibilityRule(double minOrderValue,
                                                          LocalDateTime validFrom,
                                                          LocalDateTime validUntil) {
        CompositeEligibilityRule compositeRule = new CompositeEligibilityRule();
        compositeRule.addRule(new MinOrderRule(minOrderValue));
        compositeRule.addRule(new DateRangeRule(validFrom, validUntil));
        return compositeRule;
    }
}
