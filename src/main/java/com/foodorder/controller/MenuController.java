package com.foodorder.controller;

import com.foodorder.dto.DishResponseDTO;
import com.foodorder.dto.CustomerDTO;
import com.foodorder.entity.Dish;
import com.foodorder.entity.OrderItem;
import com.foodorder.service.CartService;
import com.foodorder.service.DishService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MenuController {

    private final DishService dishService;
    private final CartService cartService;

    @Autowired
    public MenuController(DishService dishService, CartService cartService) {
        this.dishService = dishService;
        this.cartService = cartService;
    }

    @GetMapping("/menu")
    public String showMenuPage(Model model, HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        List<Dish> menu = dishService.getAllDishes();

        // Chuyển Entity → DTO trước khi đẩy ra View (3-Tier)
        List<DishResponseDTO> menuDTOs = menu.stream()
                .map(DishResponseDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("user", loggedInUser);
        model.addAttribute("menu", menuDTOs);
        model.addAttribute("cartCount", cartService.calculateCount(cart));

        return "menu";
    }

    @SuppressWarnings("unchecked")
    private List<OrderItem> getCartFromSession(HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("CART");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("CART", cart);
        }
        return cart;
    }
}
