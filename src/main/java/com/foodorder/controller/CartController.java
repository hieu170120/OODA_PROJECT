package com.foodorder.controller;

import com.foodorder.decorator.IDish;
import com.foodorder.dto.CartItemDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.dto.UserDTO;
import com.foodorder.model.Coupon;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.service.CartService;
import com.foodorder.service.CouponService;
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

import java.time.LocalDateTime;
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
    private final CouponService couponService;

    @Autowired
    public CartController(CartService cartService, OrderService orderService, CouponService couponService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.couponService = couponService;
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
    private Order buildPreviewOrder(List<OrderItem> cart) {
        Order previewOrder = new Order();
        previewOrder.setOrderTime(LocalDateTime.now());
        previewOrder.setOrderItems(cart);
        return previewOrder;
    }

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
        model.addAttribute("couponOptions", couponService.getCouponOptions(buildPreviewOrder(cart)));
        model.addAttribute("enteredCouponCode", "");

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
            @RequestParam(value = "couponCode", required = false) String couponCode,
            @RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod,
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

        Order previewOrder = buildPreviewOrder(cart);

        Coupon coupon;
        double couponDiscount = 0;
        try {
            coupon = couponService.resolveCoupon(couponCode, previewOrder);
            couponDiscount = couponService.calculateDiscount(coupon, previewOrder);
        } catch (IllegalArgumentException ex) {
            // Populate lại cart với lỗi coupon
            List<CartItemDTO> cartItemDTOs = cart.stream().map(CartItemDTO::fromDomain).collect(Collectors.toList());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cartItems", cartItemDTOs);
            model.addAttribute("cartTotal", cartService.calculateTotal(cart));
            model.addAttribute("cartCount", cartService.calculateCount(cart));
            model.addAttribute("couponOptions", couponService.getCouponOptions(previewOrder));
            model.addAttribute("enteredCouponCode", couponCode);
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }

        Order completedOrder;
        try {
            // Delegate nghiệp vụ đặt hàng cho OrderService (Builder & Strategy Pattern)
            // OrderService sẽ normalize paymentMethod string (COD, BANKING, etc.)
            completedOrder = orderService.createDeliveryOrder(customer, cart, address, coupon, couponDiscount, paymentMethod);
            couponService.recordCouponUsage(completedOrder.getCoupon());
        } catch (IllegalArgumentException ex) {
            List<CartItemDTO> cartItemDTOs = cart.stream().map(CartItemDTO::fromDomain).collect(Collectors.toList());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("cartItems", cartItemDTOs);
            model.addAttribute("cartTotal", cartService.calculateTotal(cart));
            model.addAttribute("cartCount", cartService.calculateCount(cart));
            model.addAttribute("couponOptions", couponService.getCouponOptions(previewOrder));
            model.addAttribute("enteredCouponCode", couponCode);
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }

        // Chuyển Domain → DTO trước khi đưa ra View (3-Tier)
        model.addAttribute("order", OrderResponseDTO.fromDomain(completedOrder));
        double appliedDiscount = completedOrder.getCouponDiscount();
        model.addAttribute("appliedDiscount", appliedDiscount);

        // Đặt thành công → Xóa giỏ
        session.removeAttribute("CART");

        return "order-success";
    }
}
