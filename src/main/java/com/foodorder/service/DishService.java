package com.foodorder.service;

import com.foodorder.entity.Dish;
import java.util.List;

public interface DishService {
    List<Dish> getAllDishes();
    void addDish(Dish dish);
    void updateDish(String id, Dish updatedDish);
    void deleteDish(String id);
}