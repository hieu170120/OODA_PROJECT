package com.foodorder.uml.controller;

import com.foodorder.uml.entity.Coupon;
import com.foodorder.uml.service.CouponManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uml/coupons")
public class UmlCouponController {
    private final CouponManagementService couponManagementService;

    public UmlCouponController(CouponManagementService couponManagementService) {
        this.couponManagementService = couponManagementService;
    }

    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponManagementService.getAllCoupons();
    }

    @GetMapping("/{couponId}")
    public Coupon getCouponById(@PathVariable String couponId) {
        return couponManagementService.getCouponById(couponId);
    }

    @PostMapping
    public ResponseEntity<Void> addCoupon(@RequestBody Coupon coupon) {
        couponManagementService.addCoupon(coupon);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<Void> updateCoupon(@PathVariable String couponId, @RequestBody Coupon coupon) {
        coupon.setId(couponId);
        couponManagementService.updateCoupon(coupon);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String couponId) {
        couponManagementService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
