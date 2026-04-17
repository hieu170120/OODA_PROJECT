package com.foodorder.uml.service;

import com.foodorder.uml.entity.Coupon;
import com.foodorder.uml.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponManagementService {
    private final CouponRepository couponRepository;

    public CouponManagementService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void addCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        if (!couponRepository.existsById(coupon.getId())) {
            throw new IllegalArgumentException("Coupon not found: " + coupon.getId());
        }
        couponRepository.save(coupon);
    }

    public void deleteCoupon(String couponId) {
        couponRepository.deleteById(couponId);
    }

    public Coupon getCouponById(String couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + couponId));
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
