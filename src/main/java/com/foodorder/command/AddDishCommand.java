package com.foodorder.command;

import com.foodorder.entity.Dish;
import com.foodorder.repository.DishRepository;

public class AddDishCommand implements ICommand {
    private final DishRepository repository;
    private final Dish dish;

    @Override
    public void execute() {
        System.out.println("Adding dish: " + dish.getName());
        dish.setDishId(null);
        repository.save(dish);
    }

    @Override
    public void undo() {
        System.out.println("Removing dish: " + dish.getName());
        repository.delete(dish);
    }

    public AddDishCommand(DishRepository repository, Dish dish) {
        this.repository = repository;
        this.dish = dish;
    }
}