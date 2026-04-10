package com.foodorder.repository;

import com.foodorder.entity.OrderRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderRecordEntity, String> {
}

