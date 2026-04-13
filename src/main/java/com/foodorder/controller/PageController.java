package com.foodorder.controller;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
import com.foodorder.entity.Dish;
import com.foodorder.entity.User;
import com.foodorder.model.Customer;
import com.foodorder.model.Coupon;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.enums.PaymentMethod;
import com.foodorder.service.CouponService;
import com.foodorder.service.DishService;
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
import java.time.LocalDateTime;

@Controller
public class PageController {

    private final OrderService orderService;
    private final DishService dishService;
    private final CouponService couponService;

    @Autowired
    public PageController(OrderService orderService, DishService dishService, CouponService couponService) {
        this.orderService = orderService;
        this.dishService = dishService;
        this.couponService = couponService;
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
    // QUẢN LÝ GIỎ HÀNG BẰNG SESSION
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

    private double calculateCartTotal(List<OrderItem> cart) {
        return cart.stream().mapToDouble(OrderItem::calculateSubTotal).sum();
    }

    private int calculateCartCount(List<OrderItem> cart) {
        return cart.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    private Order buildPreviewOrder(List<OrderItem> cart) {
        Order previewOrder = new Order();
        previewOrder.setOrderTime(LocalDateTime.now());
        previewOrder.setOrderItems(cart);
        return previewOrder;
    }

    private void populateCartModel(Model model, User loggedInUser, List<OrderItem> cart, String enteredCouponCode) {
        model.addAttribute("user", loggedInUser);
        model.addAttribute("cartItems", cart);
        model.addAttribute("cartTotal", calculateCartTotal(cart));
        model.addAttribute("cartCount", calculateCartCount(cart));
        model.addAttribute("couponOptions", couponService.getCouponOptions(buildPreviewOrder(cart)));
        model.addAttribute("enteredCouponCode", enteredCouponCode == null ? "" : enteredCouponCode);
    }

    @GetMapping("/menu")
    public String showMenuPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        List<Dish> menu = dishService.getAllDishes();

        model.addAttribute("user", loggedInUser);
        model.addAttribute("menu", menu);
        model.addAttribute("cartCount", calculateCartCount(cart));

        return "menu";
    }

    /**
     * Bước 2: API AJAX thêm món vào giỏ hàng (Không load lại trang)
     */
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addDishToCartAjax(
            @RequestParam("baseDish") String baseDishStr,
            @RequestParam(value = "toppings", required = false) List<String> toppings,
            @RequestParam("quantity") int quantity,
            @RequestParam(value = "imageUrl", defaultValue = "") String imageUrl,
            HttpSession session
    ) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }

        // Logic Decorator Pattern giữ nguyên
        String[] dishParts = baseDishStr.split("_");
        String dishName = dishParts[0];
        double dishPrice = Double.parseDouble(dishParts[1]);

        IDish finalDish = new BaseDish("D-" + System.currentTimeMillis(), dishName, dishPrice, imageUrl, "");

        if (toppings != null && !toppings.isEmpty()) {
            for (String topStr : toppings) {
                String[] topParts = topStr.split("_");
                String topName = topParts[0];
                double topPrice = Double.parseDouble(topParts[1]);
                finalDish = new Topping(finalDish, topName, topPrice); // Bọc Topping (Decorator)
            }
        }

        List<OrderItem> cart = getCartFromSession(session);

        // Gộp nhóm món giống nhau
        boolean found = false;
        for (OrderItem item : cart) {
            if (item.getDish().getName().equals(finalDish.getName())
                    && item.getDish().getPrice() == finalDish.getPrice()) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            OrderItem newItem = new OrderItem("OI-" + System.currentTimeMillis(), null, finalDish, quantity, finalDish.getPrice());
            cart.add(newItem);
        }

        // Trả về số lượng tổng mới cho JS cập nhật badge
        int newCartCount = calculateCartCount(cart);
        return ResponseEntity.ok(Map.of("success", true, "cartCount", newCartCount));
    }


    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        populateCartModel(model, loggedInUser, cart, "");

        return "cart";
    }

    /**
     * Xóa món khỏi giỏ (Ở trang /cart)
     */
    @PostMapping("/cart/remove-item")
    public String removeItemFromCart(@RequestParam("orderItemId") String orderItemId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        cart.removeIf(item -> item.getOrderItemId().equals(orderItemId));
        return "redirect:/cart";
    }

    /**
     * Tăng/Giảm số lượng món trong giỏ (AJAX)
     */
    @PostMapping("/cart/update-quantity")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(
            @RequestParam("orderItemId") String orderItemId,
            @RequestParam("action") String action,
            HttpSession session
    ) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }

        List<OrderItem> cart = getCartFromSession(session);

        OrderItem targetItem = null;
        for (OrderItem item : cart) {
            if (item.getOrderItemId().equals(orderItemId)) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy món"));
        }

        if ("increase".equals(action)) {
            targetItem.setQuantity(targetItem.getQuantity() + 1);
        } else if ("decrease".equals(action)) {
            targetItem.setQuantity(targetItem.getQuantity() - 1);
            if (targetItem.getQuantity() <= 0) {
                cart.remove(targetItem);
                return ResponseEntity.ok(Map.of(
                        "removed", true,
                        "cartTotal", calculateCartTotal(cart),
                        "cartCount", calculateCartCount(cart)
                ));
            }
        }

        return ResponseEntity.ok(Map.of(
                "removed", false,
                "newQuantity", targetItem.getQuantity(),
                "subTotal", targetItem.calculateSubTotal(),
                "cartTotal", calculateCartTotal(cart),
                "cartCount", calculateCartCount(cart)
        ));
    }

    /**
     * Bước 4: Xác nhận Đặt Hàng (Xử lý form ở trang /cart)
     */
    @PostMapping("/cart/checkout")
    public String processCheckout(
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam("address") String address,
            @RequestParam(value = "couponCode", required = false) String couponCode,
            @RequestParam(value = "paymentMethod", defaultValue = "COD") PaymentMethod paymentMethod,
            Model model,
            HttpSession session
    ) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");
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
        try {
            coupon = couponService.resolveCoupon(couponCode, previewOrder);
        } catch (IllegalArgumentException ex) {
            populateCartModel(model, loggedInUser, cart, couponCode);
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }

        Order completedOrder;
        try {
            // Builder & Strategy Pattern giữ nguyên
            completedOrder = orderService.createDeliveryOrder(customer, cart, address, coupon, paymentMethod);
        } catch (IllegalArgumentException ex) {
            populateCartModel(model, loggedInUser, cart, couponCode);
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }

        model.addAttribute("order", completedOrder);
        double appliedDiscount = completedOrder.getCoupon() != null
                ? completedOrder.getCoupon().calculateDiscount(completedOrder)
                : 0;
        model.addAttribute("appliedDiscount", appliedDiscount);

        // Đặt thành công -> Xóa giỏ
        session.removeAttribute("CART");

        return "order-success"; // Về trang biên lai
    }
    
    // Giữ lại endpoint /order cũ redirect về /menu để tương thích ngược nếu cần
    @GetMapping("/order")
    public String showOrderPage() {
        return "redirect:/menu";
    }
}
