package com.foodorder.uml.service;

import com.foodorder.uml.entity.Dish;
import com.foodorder.uml.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuManagementService {
    private final DishRepository dishRepository;

    public MenuManagementService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public void addDish(Dish dish) {
        dishRepository.save(dish);
    }

    public void updateDish(String dishId, Dish updatedDish) {
        Dish existingDish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found: " + dishId));
        existingDish.setName(updatedDish.getName());
        existingDish.setPrice(updatedDish.getPrice());
        existingDish.setImage(updatedDish.getImage());
        existingDish.setDescription(updatedDish.getDescription());
        dishRepository.save(existingDish);
    }

    public void deleteDish(String dishId) {
        dishRepository.deleteById(dishId);
    }

    public Dish getDishById(String dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found: " + dishId));
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }
}
