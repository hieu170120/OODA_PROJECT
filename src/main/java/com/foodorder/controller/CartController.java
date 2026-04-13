package com.foodorder.controller;

import com.foodorder.decorator.IDish;
import com.foodorder.dto.CartItemDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.dto.UserDTO;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.enums.PaymentMethod;
import com.foodorder.service.CartService;
import com.foodorder.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller quản lý giỏ hàng và thanh toán.
 * Tất cả business logic đã được ủy quyền (delegate) cho CartService / OrderService.
 */
@Controller
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    // ==========================================
    // LẤY GIỎ HÀNG TỪ SESSION
    // ==========================================
    @SuppressWarnings("unchecked")
    private List<OrderItem> getCartFromSession(HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("CART");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("CART", cart);
        }
        return cart;
    }

    // ==========================================
    // HIỂN THỊ TRANG GIỎ HÀNG
    // ==========================================
    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);

        // Chuyển Domain → DTO trước khi đưa ra View (3-Tier)
        List<CartItemDTO> cartItemDTOs = cart.stream()
                .map(CartItemDTO::fromDomain)
                .collect(Collectors.toList());

        model.addAttribute("user", loggedInUser);
        model.addAttribute("cartItems", cartItemDTOs);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        model.addAttribute("cartCount", cartService.calculateCount(cart));

        return "cart";
    }

    // ==========================================
    // API AJAX: THÊM MÓN VÀO GIỎ
    // ==========================================
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addDishToCartAjax(
            @RequestParam("baseDish") String baseDishStr,
            @RequestParam(value = "toppings", required = false) List<String> toppings,
            @RequestParam("quantity") int quantity,
            @RequestParam(value = "imageUrl", defaultValue = "") String imageUrl,
            HttpSession session
    ) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }

        // Delegate business logic cho CartService
        IDish finalDish = cartService.createDishWithToppings(baseDishStr, toppings, imageUrl);

        List<OrderItem> cart = getCartFromSession(session);
        cartService.addItemToCart(cart, finalDish, quantity);

        int newCartCount = cartService.calculateCount(cart);
        return ResponseEntity.ok(Map.of("success", true, "cartCount", newCartCount));
    }

    // ==========================================
    // XÓA MÓN KHỎI GIỎ
    // ==========================================
    @PostMapping("/cart/remove-item")
    public String removeItemFromCart(@RequestParam("orderItemId") String orderItemId, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        cartService.removeItem(cart, orderItemId);
        return "redirect:/cart";
    }

    // ==========================================
    // TĂNG/GIẢM SỐ LƯỢNG (AJAX)
    // ==========================================
    @PostMapping("/cart/update-quantity")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(
            @RequestParam("orderItemId") String orderItemId,
            @RequestParam("action") String action,
            HttpSession session
    ) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }

        List<OrderItem> cart = getCartFromSession(session);

        // Delegate tìm kiếm cho CartService
        OrderItem targetItem = cartService.findItem(cart, orderItemId);
        if (targetItem == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy món"));
        }

        if ("increase".equals(action)) {
            cartService.increaseQuantity(targetItem);
        } else if ("decrease".equals(action)) {
            boolean removed = cartService.decreaseQuantity(cart, targetItem);
            if (removed) {
                return ResponseEntity.ok(Map.of(
                        "removed", true,
                        "cartTotal", cartService.calculateTotal(cart),
                        "cartCount", cartService.calculateCount(cart)
                ));
            }
        }

        return ResponseEntity.ok(Map.of(
                "removed", false,
                "newQuantity", targetItem.getQuantity(),
                "subTotal", targetItem.calculateSubTotal(),
                "cartTotal", cartService.calculateTotal(cart),
                "cartCount", cartService.calculateCount(cart)
        ));
    }

    // ==========================================
    // XÁC NHẬN ĐẶT HÀNG (CHECKOUT)
    // ==========================================
    @PostMapping("/cart/checkout")
    public String processCheckout(
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam("address") String address,
            @RequestParam(value = "paymentMethod", defaultValue = "COD") PaymentMethod paymentMethod,
            Model model,
            HttpSession session
    ) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        Customer customer = new Customer();
        customer.setUserId(loggedInUser.getUserId() != null ? loggedInUser.getUserId() : "CUST-" + System.currentTimeMillis());
        customer.setFullName((customerName != null && !customerName.isBlank()) ? customerName : loggedInUser.getFullName());

        Order completedOrder;
        try {
            // Delegate nghiệp vụ đặt hàng cho OrderService (Builder & Strategy Pattern)
            completedOrder = orderService.createDeliveryOrder(customer, cart, address, null, paymentMethod);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cartItems", cart.stream().map(CartItemDTO::fromDomain).collect(Collectors.toList()));
            model.addAttribute("cartTotal", cartService.calculateTotal(cart));
            model.addAttribute("cartCount", cartService.calculateCount(cart));
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }

        // Chuyển Domain → DTO trước khi đưa ra View (3-Tier)
        model.addAttribute("order", OrderResponseDTO.fromDomain(completedOrder));

        // Đặt thành công → Xóa giỏ
        session.removeAttribute("CART");

        return "order-success";
    }
}
