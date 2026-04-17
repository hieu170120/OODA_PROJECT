package com.foodorder.uml.repository;

import com.foodorder.uml.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, String> {
}
