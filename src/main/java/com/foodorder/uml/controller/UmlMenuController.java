package com.foodorder.uml.controller;

import com.foodorder.uml.entity.Dish;
import com.foodorder.uml.service.MenuManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uml/menu")
public class UmlMenuController {
    private final MenuManagementService menuManagementService;

    public UmlMenuController(MenuManagementService menuManagementService) {
        this.menuManagementService = menuManagementService;
    }

    @GetMapping("/dishes")
    public List<Dish> getAllDishes() {
        return menuManagementService.getAllDishes();
    }

    @GetMapping("/dishes/{dishId}")
    public Dish getDishById(@PathVariable String dishId) {
        return menuManagementService.getDishById(dishId);
    }

    @PostMapping("/dishes")
    public ResponseEntity<Void> addDish(@RequestBody Dish dish) {
        menuManagementService.addDish(dish);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/dishes/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable String dishId, @RequestBody Dish dish) {
        menuManagementService.updateDish(dishId, dish);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dishes/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable String dishId) {
        menuManagementService.deleteDish(dishId);
        return ResponseEntity.noContent().build();
    }
}
