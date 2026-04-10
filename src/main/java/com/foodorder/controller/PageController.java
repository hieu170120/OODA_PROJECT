package com.foodorder.controller;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.enums.PaymentMethod;
import com.foodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    private final OrderService orderService;
    
    // Lưu tạm giỏ hàng trong bộ nhớ RAM cho từng phiên làm việc đơn giản (Session giả lập)
    private List<OrderItem> tempCart = new ArrayList<>();

    @Autowired
    public PageController(OrderService orderService) {
        this.orderService = orderService;
    }

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

    // ==========================================
    // CÁC TRANG DEMO DESIGN PATTERN (MỚI THÊM)
    // ==========================================

    /**
     * Mở trang Web giao diện Order
     */
    @GetMapping("/order")
    public String showOrderPage(Model model) {
        model.addAttribute("cartItems", tempCart);
        return "order"; 
    }

    /**
     * Xử lý Nút: "Thêm vào Đơn Hàng" (DEMO DECORATOR)
     */
    @PostMapping("/order/add-item")
    public String addItemToCart(
            @RequestParam("baseDish") String baseDishStr, 
            @RequestParam(value = "toppings", required = false) List<String> toppings, 
            @RequestParam("quantity") int quantity
    ) {
        String[] dishParts = baseDishStr.split("_");
        String dishName = dishParts[0];
        double dishPrice = Double.parseDouble(dishParts[1]);

        IDish finalDish = new BaseDish("D-" + System.currentTimeMillis(), dishName, dishPrice, "", "");

        if (toppings != null && !toppings.isEmpty()) {
            for (String topStr : toppings) {
                String[] topParts = topStr.split("_");
                String topName = topParts[0];
                double topPrice = Double.parseDouble(topParts[1]);
                finalDish = new Topping(finalDish, topName, topPrice);
            }
        }

        OrderItem newItem = new OrderItem("OI-" + System.currentTimeMillis(), null, finalDish, quantity, finalDish.getPrice());
        tempCart.add(newItem);

        return "redirect:/order";
    }

    /**
     * Xử lý Nút: "Xác nhận Đặt Hàng" (DEMO BUILDER)
     */
    @PostMapping("/order/checkout")
    public String processCheckout(
            @RequestParam("customerName") String customerName,
            @RequestParam("address") String address,
            @RequestParam(value = "paymentMethod", defaultValue = "COD") PaymentMethod paymentMethod,
            Model model
    ) {
        if (tempCart.isEmpty()) {
            return "redirect:/order";
        }

        Customer customer = new Customer();
        customer.setUserId("CUST-WEB-" + System.currentTimeMillis());
        customer.setFullName(customerName);

        Order completedOrder;
        try {
            completedOrder = orderService.createDeliveryOrder(customer, tempCart, address, null, paymentMethod);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("cartItems", tempCart);
            model.addAttribute("checkoutError", ex.getMessage());
            return "order";
        }

        model.addAttribute("order", completedOrder);
        
        tempCart = new ArrayList<>();

        return "order-success";
    }
}
