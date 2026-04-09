package com.foodorder.controller;

import com.foodorder.command.Manager; // <--- Import thêm Manager
import com.foodorder.entity.Dish;
import com.foodorder.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDishController {

    private final DishService dishService;
    private final Manager commandManager; // <--- Tiêm Manager trực tiếp vào Controller

    @GetMapping("/dishes")
    public String showDishList(Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        return "admin/dish-list"; 
    }

    // --- CÁC HÀM CỦA DISH SERVICE ---

    @PostMapping("/add")
    public String addDish(@ModelAttribute Dish dish) {
        dishService.addDish(dish);
        return "redirect:/admin/dishes"; 
    }

    @PostMapping("/update/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute Dish updatedDish) {
        dishService.updateDish(id, updatedDish);
        return "redirect:/admin/dishes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable String id) {
        dishService.deleteDish(id);
        return "redirect:/admin/dishes"; 
    }

    // --- CÁC HÀM CỦA HỆ THỐNG (DO MANAGER QUẢN LÝ) ---

    @GetMapping("/undo")
    public String undoAction() {
        // Gọi trực tiếp Manager để Undo, không làm phiền DishService
        commandManager.undoLastAction();
        return "redirect:/admin/dishes";
    }

    @GetMapping("/redo")
    public String redoAction() {
        // Gọi trực tiếp Manager để Redo
        commandManager.redoLastAction();
        return "redirect:/admin/dishes";
    }
}