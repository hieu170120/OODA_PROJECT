package com.foodorder.command;

import com.foodorder.entity.Dish;
import com.foodorder.repository.DishRepository;

import lombok.AllArgsConstructor;

public class UpdateDishCommand implements ICommand {
    private final DishRepository repository;
    private final Dish dishInDB;
    private final Dish oldDish;
    private final Dish newDish;

    public UpdateDishCommand(DishRepository repository, Dish dishInDB, Dish newDish) {
        this.repository = repository;
        this.dishInDB = dishInDB;
        this.newDish = newDish;

        this.oldDish = new Dish(
            dishInDB.getDishId(), 
            dishInDB.getName(), 
            dishInDB.getPrice(), 
            dishInDB.getImageUrl(),
            dishInDB.getDescription()
        );
    }

    @Override   
    public void execute() {
        System.out.println("Updating dish: " + oldDish.getName());

        this.dishInDB.setName(newDish.getName());
        this.dishInDB.setPrice(newDish.getPrice());
        this.dishInDB.setImageUrl(newDish.getImageUrl());
        this.dishInDB.setDescription(newDish.getDescription());

        repository.save(dishInDB);
    }

    @Override
    public void undo() {
        System.out.println("Reverting update for dish: " + newDish.getName());

        this.dishInDB.setName(oldDish.getName());
        this.dishInDB.setPrice(oldDish.getPrice());
        this.dishInDB.setImageUrl(oldDish.getImageUrl());
        this.dishInDB.setDescription(oldDish.getDescription());

        repository.save(dishInDB);
    }
}