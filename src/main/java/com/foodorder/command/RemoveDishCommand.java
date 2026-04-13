package com.foodorder.command;

import com.foodorder.entity.Dish;
import com.foodorder.repository.DishRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveDishCommand implements ICommand {
    private final DishRepository repository;
    private final Dish dish;

    @Override   
    public void execute() {
        System.out.println("Removing dish: " + dish.getName());
        repository.delete(dish);
    }

    @Override
    public void undo() {
        System.out.println("Restoring dish: " + dish.getName());
        dish.setDishId(null);
        repository.save(dish);
    }
}