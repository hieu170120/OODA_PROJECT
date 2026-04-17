package com.foodorder.uml.repository;

import com.foodorder.uml.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
