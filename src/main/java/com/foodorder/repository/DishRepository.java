package com.foodorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  com.foodorder.entity.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, String> {
    
}
