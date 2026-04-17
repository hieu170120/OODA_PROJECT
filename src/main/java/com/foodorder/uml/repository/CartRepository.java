package com.foodorder.uml.repository;

import com.foodorder.uml.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
}
