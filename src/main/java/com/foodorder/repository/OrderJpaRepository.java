package com.foodorder.repository;

import com.foodorder.entity.OrderRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderRecordEntity, String> {
    List<OrderRecordEntity> findAllByOrderByOrderTimeDesc();
}

