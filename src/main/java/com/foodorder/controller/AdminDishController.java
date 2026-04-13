package com.foodorder.controller;

import com.foodorder.command.Manager; 
import com.foodorder.dto.DishRequestDTO;
import com.foodorder.dto.DishResponseDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.entity.Dish;
import com.foodorder.model.enums.OrderStatus;
import com.foodorder.service.DishService;
import com.foodorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDishController {

    private final DishService dishService;
    private final Manager commandManager; 
    private final OrderService orderService;

    @GetMapping("/dishes")
    public String showDishList(Model model) {
        // Ánh xạ danh sách Entity thành DTO trước khi đẩy ra View
        List<DishResponseDTO> dishDTOs = dishService.getAllDishes().stream()
                .map(DishResponseDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("dishes", dishDTOs);
        model.addAttribute("canUndo", commandManager.canUndo());
        model.addAttribute("canRedo", commandManager.canRedo());
        return "admin/dish-list"; 
    }

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        // Chuyển Domain Model → DTO trước khi đẩy ra View (3-Tier)
        List<OrderResponseDTO> orderDTOs = orderService.getAllOrders().stream()
                .map(OrderResponseDTO::fromDomain)
                .collect(Collectors.toList());

        model.addAttribute("orders", orderDTOs);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId,
                                    @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }

    // --- CÁC HÀM CỦA DISH SERVICE ---

    @PostMapping("/add")
    public String addDish(@ModelAttribute DishRequestDTO dishDTO) {
        // Chuyển DTO thành Entity rồi mới đưa vào Service
        dishService.addDish(dishDTO.toEntity());
        return "redirect:/admin/dishes"; 
    }

    @PostMapping("/update/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute DishRequestDTO updatedDishDTO) {
        // Chuyển DTO thành Entity rồi mới đưa vào Service
        dishService.updateDish(id, updatedDishDTO.toEntity());
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
        commandManager.undoLastAction();
        return "redirect:/admin/dishes";
    }

    @GetMapping("/redo")
    public String redoAction() {
        commandManager.redoLastAction();
        return "redirect:/admin/dishes";
    }
}