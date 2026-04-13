package com.foodorder.controller.admin;

import com.foodorder.command.Manager;
import com.foodorder.dto.DishRequestDTO;
import com.foodorder.dto.DishResponseDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.model.Order;
import com.foodorder.model.enums.OrderStatus;
import com.foodorder.service.DishService;
import com.foodorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller quản lý các chức năng admin: dishes, orders, dashboard
 * Xử lý tất cả các hoạt động của quản trị viên trên hệ thống
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDishController {

    private final DishService dishService;
    private final Manager commandManager;
    private final OrderService orderService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        // Thêm dữ liệu thống kê cho dashboard
        long totalDishes = dishService.getAllDishes().size();
        long totalOrders = orderService.getAllOrders().size();

        // Tính orders hôm nay và doanh thu hôm nay
        long ordersToday = 0;
        double revenueToday = 0.0;
        java.time.LocalDate today = java.time.LocalDate.now();

        for (Order order : orderService.getAllOrders()) {
            if (order.getOrderTime() != null &&
                    order.getOrderTime().toLocalDate().equals(today)) {
                ordersToday++;
                revenueToday += order.calculateTotal();
            }
        }

        model.addAttribute("totalDishes", totalDishes);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("ordersToday", ordersToday);
        model.addAttribute("revenueToday", String.format("%.0fđ", revenueToday));

        return "admin/dashboard";
    }

    @GetMapping("/dishes")
    public String showDishList(Model model, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

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
    public String showOrderList(Model model, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

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
                                    @RequestParam("status") OrderStatus status,
                                    HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }

    // --- CÁC HÀM CỦA DISH SERVICE ---

    @PostMapping("/add")
    public String addDish(@ModelAttribute DishRequestDTO dishDTO, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        // Chuyển DTO thành Entity rồi mới đưa vào Service
        dishService.addDish(dishDTO.toEntity());
        return "redirect:/admin/dishes";
    }

    @PostMapping("/update/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute DishRequestDTO updatedDishDTO, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        // Chuyển DTO thành Entity rồi mới đưa vào Service
        dishService.updateDish(id, updatedDishDTO.toEntity());
        return "redirect:/admin/dishes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable String id, HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        dishService.deleteDish(id);
        return "redirect:/admin/dishes";
    }

    // --- CÁC HÀM CỦA HỆ THỐNG (DO MANAGER QUẢN LÝ) ---

    @GetMapping("/undo")
    public String undoAction(HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        commandManager.undoLastAction();
        return "redirect:/admin/dishes";
    }

    @GetMapping("/redo")
    public String redoAction(HttpSession session) {
        // Kiểm tra xem manager đã đăng nhập chưa
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        commandManager.redoLastAction();
        return "redirect:/admin/dishes";
    }
}
