package com.foodorder.uml.repository;

import com.foodorder.uml.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToppingRepository extends JpaRepository<Topping, String> {
}
