package com.foodorder.service;

import com.foodorder.dto.CouponRequestDTO;
import com.foodorder.dto.CouponResponseDTO;
import com.foodorder.entity.CouponEntity;
import com.foodorder.model.Coupon;
import com.foodorder.model.Order;
import com.foodorder.repository.CouponRepository;
import com.foodorder.specification.CompositeEligibilityRule;
import com.foodorder.specification.DateRangeRule;
import com.foodorder.specification.EligibilityRule;
import com.foodorder.specification.MinOrderRule;
import com.foodorder.strategy.coupon.DiscountStrategy;
import com.foodorder.strategy.coupon.FixedAmountDiscountStrategy;
import com.foodorder.strategy.coupon.PercentageDiscountStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        String normalizedCode = rawCode.trim().toUpperCase();
        CouponEntity couponEntity = couponRepository.findByCouponCodeIgnoreCase(normalizedCode).orElse(null);
        Coupon coupon = toDomain(couponEntity);

        if (coupon == null) {
            throw new IllegalArgumentException("Mã coupon không tồn tại.");
        }
        if (!isCouponAvailable(couponEntity)) {
            throw new IllegalArgumentException("Mã coupon hiện không khả dụng.");
        }
        if (!isEligible(couponEntity, previewOrder)) {
            throw new IllegalArgumentException("Mã coupon chưa đủ điều kiện áp dụng.");
        }

        return coupon;
    }

    public List<CouponOption> getCouponOptions(Order previewOrder) {
        List<CouponOption> options = new ArrayList<>();
        for (CouponEntity entity : couponRepository.findByActiveTrueOrderByCouponCodeAsc()) {
            Coupon coupon = toDomain(entity);
            if (coupon != null) {
                options.add(buildOption(coupon, entity, previewOrder));
            }
        }
        return options;
    }

    @Transactional(readOnly = true)
    public List<CouponResponseDTO> getAllCoupons() {
        List<CouponResponseDTO> result = new ArrayList<>();
        for (CouponEntity entity : couponRepository.findAllByOrderByCouponCodeAsc()) {
            result.add(CouponResponseDTO.fromEntity(entity));
        }
        return result;
    }

    @Transactional
    public void createCoupon(CouponRequestDTO requestDTO) {
        validateCouponRequest(requestDTO);

        if (couponRepository.findByCouponCodeIgnoreCase(requestDTO.getCouponCode()).isPresent()) {
            throw new IllegalArgumentException("Mã coupon đã tồn tại.");
        }

        CouponEntity entity = requestDTO.toEntity();
        entity.setCouponCode(entity.getCouponCode().trim().toUpperCase());
        entity.setUsedCount(0);
        couponRepository.save(entity);
    }

    @Transactional
    public void updateCoupon(String couponId, CouponRequestDTO requestDTO) {
        validateCouponRequest(requestDTO);

        CouponEntity existing = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy coupon."));

        couponRepository.findByCouponCodeIgnoreCase(requestDTO.getCouponCode())
                .ifPresent(found -> {
                    if (!Objects.equals(found.getCouponId(), couponId)) {
                        throw new IllegalArgumentException("Mã coupon đã tồn tại.");
                    }
                });

        existing.setCouponCode(requestDTO.getCouponCode().trim().toUpperCase());
        existing.setDiscountValue(requestDTO.getDiscountValue());
        existing.setMinOrderValue(requestDTO.getMinOrderValue());
        existing.setPercentage(requestDTO.isPercentage());
        existing.setMaxDiscount(requestDTO.isPercentage() ? requestDTO.getMaxDiscount() : null);
        existing.setValidFrom(requestDTO.getValidFrom());
        existing.setValidUntil(requestDTO.getValidUntil());
        existing.setActive(requestDTO.isActive());
        existing.setUsageLimit(requestDTO.getUsageLimit());
        couponRepository.save(existing);
    }

    @Transactional
    public void deleteCoupon(String couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new IllegalArgumentException("Không tìm thấy coupon để xóa.");
        }
        couponRepository.deleteById(couponId);
    }

    @Transactional
    public void recordCouponUsage(Coupon coupon) {
        if (coupon == null || coupon.getCouponId() == null || coupon.getCouponId().isBlank()) {
            return;
        }

        couponRepository.findById(coupon.getCouponId()).ifPresent(entity -> {
            int currentUsed = entity.getUsedCount() != null ? entity.getUsedCount() : 0;
            entity.setUsedCount(currentUsed + 1);
            couponRepository.save(entity);
        });
    }

    private CouponOption buildOption(Coupon coupon, CouponEntity entity, Order previewOrder) {
        String description = buildDescription(entity);

        if (!isCouponAvailable(entity)) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Coupon đã hết lượt sử dụng.");
        }

        if (previewOrder == null) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Chưa có dữ liệu đơn hàng để kiểm tra điều kiện.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (entity.getValidFrom() != null && now.isBefore(entity.getValidFrom())) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Coupon chưa tới thời gian áp dụng.");
        }
        if (entity.getValidUntil() != null && now.isAfter(entity.getValidUntil())) {
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Coupon đã hết hạn.");
        }

        double subtotal = previewOrder.calculateSubtotalAmount();
        if (subtotal < entity.getMinOrderValue()) {
            double missing = entity.getMinOrderValue() - subtotal;
            return new CouponOption(coupon.getCouponCode(), description, false, 0,
                    "Cần mua thêm " + formatMoney(missing) + "đ để dùng mã này.");
        }

        double estimated = calculateDiscount(entity, previewOrder);
        return new CouponOption(coupon.getCouponCode(), description, true, estimated,
                "Đủ điều kiện áp dụng.");
    }

    private String buildDescription(CouponEntity entity) {
        if (entity.isPercentage()) {
            double maxDiscount = entity.getMaxDiscount() != null ? entity.getMaxDiscount() : 0;
            if (maxDiscount > 0) {
                return String.format("Giảm %.0f%%, tối đa %sđ (đơn từ %sđ)",
                        entity.getDiscountValue(),
                        formatMoney(maxDiscount),
                        formatMoney(entity.getMinOrderValue()));
            }
            return String.format("Giảm %.0f%% (đơn từ %sđ)",
                    entity.getDiscountValue(),
                    formatMoney(entity.getMinOrderValue()));
        }

        return String.format("Giảm %sđ (đơn từ %sđ)",
                formatMoney(entity.getDiscountValue()),
                formatMoney(entity.getMinOrderValue()));
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f", Math.max(amount, 0));
    }

    public double calculateDiscount(Coupon coupon, Order order) {
        if (coupon == null || coupon.getCouponId() == null || coupon.getCouponId().isBlank()) {
            return 0;
        }
        CouponEntity entity = couponRepository.findById(coupon.getCouponId()).orElse(null);
        return calculateDiscount(entity, order);
    }

    private double calculateDiscount(CouponEntity entity, Order order) {
        if (!isEligible(entity, order)) {
            return 0;
        }

        DiscountStrategy strategy = buildDiscountStrategy(entity);
        if (strategy == null) {
            return 0;
        }

        double discount = strategy.calculateDiscount(order);
        if (discount < 0) {
            return 0;
        }
        return Math.min(discount, order.calculateSubtotalAmount());
    }

    private boolean isEligible(CouponEntity entity, Order order) {
        if (entity == null || order == null) {
            return false;
        }
        EligibilityRule rule = buildEligibilityRule(
                entity.getMinOrderValue(),
                entity.getValidFrom(),
                entity.getValidUntil()
        );
        return rule.isSatisfiedBy(order);
    }

    private DiscountStrategy buildDiscountStrategy(CouponEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity.isPercentage()) {
            return new PercentageDiscountStrategy(
                    entity.getDiscountValue(),
                    entity.getMaxDiscount() != null ? entity.getMaxDiscount() : 0
            );
        }
        return new FixedAmountDiscountStrategy(entity.getDiscountValue());
    }

    private CompositeEligibilityRule buildEligibilityRule(double minOrderValue,
                                                          LocalDateTime validFrom,
                                                          LocalDateTime validUntil) {
        CompositeEligibilityRule compositeRule = new CompositeEligibilityRule();
        compositeRule.addRule(new MinOrderRule(minOrderValue));
        compositeRule.addRule(new DateRangeRule(validFrom, validUntil));
        return compositeRule;
    }

    private Coupon toDomain(CouponEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Coupon(
                entity.getCouponId(),
                entity.getCouponCode()
        );
    }

    private boolean isCouponAvailable(CouponEntity entity) {
        if (entity == null || !entity.isActive()) {
            return false;
        }

        Integer limit = entity.getUsageLimit();
        if (limit == null || limit <= 0) {
            return true;
        }

        int used = entity.getUsedCount() != null ? entity.getUsedCount() : 0;
        return used < limit;
    }

    private void validateCouponRequest(CouponRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new IllegalArgumentException("Dữ liệu coupon không hợp lệ.");
        }
        if (requestDTO.getCouponCode() == null || requestDTO.getCouponCode().isBlank()) {
            throw new IllegalArgumentException("Mã coupon không được để trống.");
        }
        if (requestDTO.getDiscountValue() <= 0) {
            throw new IllegalArgumentException("Giá trị giảm phải lớn hơn 0.");
        }
        if (requestDTO.getMinOrderValue() < 0) {
            throw new IllegalArgumentException("Đơn tối thiểu không hợp lệ.");
        }
        if (requestDTO.getValidFrom() != null && requestDTO.getValidUntil() != null
                && requestDTO.getValidFrom().isAfter(requestDTO.getValidUntil())) {
            throw new IllegalArgumentException("Thời gian hiệu lực không hợp lệ.");
        }
        if (requestDTO.isPercentage() && requestDTO.getDiscountValue() > 100) {
            throw new IllegalArgumentException("Coupon phần trăm không được vượt quá 100.");
        }
        if (requestDTO.getUsageLimit() != null && requestDTO.getUsageLimit() < 0) {
            throw new IllegalArgumentException("Số lượt sử dụng không hợp lệ.");
        }
    }
}
