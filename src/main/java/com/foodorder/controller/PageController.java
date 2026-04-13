package com.foodorder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller điều hướng trang cơ bản.
 * Tất cả logic nghiệp vụ đã được chuyển sang:
 *   - MenuController  → GET /menu
 *   - CartController   → GET /cart, POST /api/cart/add, POST /cart/*, checkout
 *   - AuthController   → POST /login, GET /logout
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String landing() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Giữ lại endpoint /order cũ redirect về /menu để tương thích ngược
    @GetMapping("/order")
    public String showOrderPage() {
        return "redirect:/menu";
    }
}
