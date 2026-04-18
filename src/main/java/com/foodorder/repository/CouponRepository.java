package com.foodorder.repository;

import com.foodorder.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, String> {
    Optional<CouponEntity> findByCouponCodeIgnoreCase(String couponCode);

    List<CouponEntity> findByActiveTrueOrderByCouponCodeAsc();

    List<CouponEntity> findAllByOrderByCouponCodeAsc();
}
