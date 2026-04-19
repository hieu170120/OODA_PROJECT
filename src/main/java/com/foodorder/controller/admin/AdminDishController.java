package com.foodorder.controller.admin;

import com.foodorder.command.CommandManager;
import com.foodorder.dto.DishRequestDTO;
import com.foodorder.dto.DishResponseDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.entity.Order;
import com.foodorder.enums.OrderStatus;
import com.foodorder.service.DishService;
import com.foodorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDishController {

    private final DishService dishService;
    private final CommandManager commandManager;
    private final OrderService orderService;

    public AdminDishController(DishService dishService, CommandManager commandManager, OrderService orderService) {
        this.dishService = dishService;
        this.commandManager = commandManager;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        long totalDishes = dishService.getAllDishes().size();
        long totalOrders = orderService.getAllOrders().size();
        long ordersToday = 0;
        double revenueToday = 0.0;
        LocalDate today = LocalDate.now();

        for (Order order : orderService.getAllOrders()) {
            if (order.getOrderTime() != null && order.getOrderTime().toLocalDate().equals(today)) {
                ordersToday++;
                revenueToday += order.calculateTotal();
            }
        }

        model.addAttribute("totalDishes", totalDishes);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("ordersToday", ordersToday);
        model.addAttribute("revenueToday", String.format("%.0fd", revenueToday));
        return "admin/dashboard";
    }

    @GetMapping("/dishes")
    public String showDishList(Model model, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        List<DishResponseDTO> dishDTOs = dishService.getAllDishes().stream()
                .map(DishResponseDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("dishes", dishDTOs);
        model.addAttribute("canUndo", commandManager.canUndo());
        model.addAttribute("canRedo", commandManager.canRedo());
        return "admin/dish-list";
    }

    @PostMapping("/add")
    public String addDish(@ModelAttribute DishRequestDTO dishDTO, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        dishService.addDish(dishDTO.toEntity());
        return "redirect:/admin/dishes";
    }

    @PostMapping("/update/{id}")
    public String updateDish(@PathVariable String id,
                             @ModelAttribute DishRequestDTO updatedDishDTO,
                             HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        dishService.updateDish(id, updatedDishDTO.toEntity());
        return "redirect:/admin/dishes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        dishService.deleteDish(id);
        return "redirect:/admin/dishes";
    }

    @GetMapping("/undo")
    public String undoAction(HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        commandManager.undoLastAction();
        return "redirect:/admin/dishes";
    }

    @GetMapping("/redo")
    public String redoAction(HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        commandManager.redoLastAction();
        return "redirect:/admin/dishes";
    }

    @GetMapping("/orders")
    public String showOrderList(Model model, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        List<OrderResponseDTO> orderDTOs = orderService.getAllOrders().stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("orders", orderDTOs);
        model.addAttribute("selectableByStatus", orderService.getOrderStatusSelectOptions());
        return "admin/orders";
    }

    @PostMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId,
                                    @RequestParam("status") OrderStatus status,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        try {
            orderService.updateOrderStatus(orderId, status);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/orders";
    }

    
}
