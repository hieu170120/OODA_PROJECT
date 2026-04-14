package com.foodorder.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.foodorder.command.AddDishCommand;
import com.foodorder.command.CommandManager;
import com.foodorder.command.RemoveDishCommand;
import com.foodorder.command.UpdateDishCommand;
import com.foodorder.entity.Dish;
import com.foodorder.repository.DishRepository;
import com.foodorder.service.DishService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final CommandManager commandManager;

    @Override
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public void addDish(Dish dish) {
        commandManager.executeCommand(new AddDishCommand(dishRepository, dish));
    }

    @Override
    public void updateDish(String id, Dish updatedDish) {
        Dish dishInDB = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        commandManager.executeCommand(new UpdateDishCommand(dishRepository, dishInDB, updatedDish));
    }

    @Override
    public void deleteDish(String id) {
        Dish dishInDB = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        commandManager.executeCommand(new RemoveDishCommand(dishRepository, dishInDB));
    }

}