package com.foodorder.controller;

import com.foodorder.dto.CartItemDTO;
import com.foodorder.dto.CustomerDTO;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.entity.Coupon;
import com.foodorder.entity.Customer;
import com.foodorder.entity.Dish;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.service.CartService;
import com.foodorder.service.CouponService;
import com.foodorder.service.OrderService;
import com.foodorder.service.AddressService;
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

@Controller
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final AddressService addressService;

    @Autowired
    public CartController(CartService cartService, OrderService orderService, CouponService couponService,
                          AddressService addressService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.couponService = couponService;
        this.addressService = addressService;
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

    private Order buildPreviewOrder(List<OrderItem> cart) {
        Order previewOrder = new Order();
        previewOrder.setOrderTime(LocalDateTime.now());
        previewOrder.setOrderItems(cart);
        return previewOrder;
    }

    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<OrderItem> cart = getCartFromSession(session);
        List<CartItemDTO> cartItemDTOs = cart.stream()
                .map(CartItemDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("user", loggedInUser);
        model.addAttribute("cartItems", cartItemDTOs);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        model.addAttribute("cartCount", cartService.calculateCount(cart));
        model.addAttribute("couponOptions", couponService.getCouponOptions(buildPreviewOrder(cart)));
        model.addAttribute("enteredCouponCode", "");
        model.addAttribute("addressOptions", addressService.getAddressOptions(loggedInUser.getUserId()));
        model.addAttribute("selectedAddressId", "");
        model.addAttribute("enteredAddress", "");
        return "cart";
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addDishToCartAjax(@RequestParam("baseDish") String baseDishStr,
                                               @RequestParam(value = "toppings", required = false) List<String> toppings,
                                               @RequestParam("quantity") int quantity,
                                               @RequestParam(value = "imageUrl", defaultValue = "") String imageUrl,
                                               HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chua dang nhap"));
        }

        Dish finalDish = cartService.createDishWithToppings(baseDishStr, toppings, imageUrl);
        List<OrderItem> cart = getCartFromSession(session);
        cartService.addItemToCart(cart, finalDish, quantity);
        return ResponseEntity.ok(Map.of("success", true, "cartCount", cartService.calculateCount(cart)));
    }

    @PostMapping("/cart/remove-item")
    public String removeItemFromCart(@RequestParam("orderItemId") String orderItemId, HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        cartService.removeItem(getCartFromSession(session), orderItemId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update-quantity")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(@RequestParam("orderItemId") String orderItemId,
                                                    @RequestParam("action") String action,
                                                    HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Chua dang nhap"));
        }

        List<OrderItem> cart = getCartFromSession(session);
        OrderItem targetItem = cartService.findItem(cart, orderItemId);
        if (targetItem == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Khong tim thay mon"));
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

    @PostMapping("/cart/checkout")
    public String processCheckout(@RequestParam(value = "customerName", required = false) String customerName,
                                  @RequestParam(value = "selectedAddressId", required = false) String selectedAddressId,
                                  @RequestParam("address") String address,
                                  @RequestParam(value = "couponCode", required = false) String couponCode,
                                  @RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod,
                                  Model model,
                                  HttpSession session) {
        CustomerDTO loggedInUser = (CustomerDTO) session.getAttribute("LOGGED_IN_USER");
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

        String resolvedAddress;
        try {
            resolvedAddress = addressService.resolveCheckoutAddress(customer.getUserId(), selectedAddressId, address);
        } catch (IllegalArgumentException ex) {
            repopulateCart(model, loggedInUser, cart, buildPreviewOrder(cart), couponCode, selectedAddressId,
                    address, ex.getMessage());
            return "cart";
        }

        Order previewOrder = buildPreviewOrder(cart);
        Coupon coupon;
        try {
            coupon = couponService.resolveCoupon(couponCode, previewOrder);
        } catch (IllegalArgumentException ex) {
            repopulateCart(model, loggedInUser, cart, previewOrder, couponCode, selectedAddressId,
                    address, ex.getMessage());
            return "cart";
        }

        Order completedOrder;
        try {
            completedOrder = orderService.createDeliveryOrder(customer, cart, resolvedAddress, coupon, paymentMethod);
        } catch (IllegalArgumentException ex) {
            repopulateCart(model, loggedInUser, cart, previewOrder, couponCode, selectedAddressId,
                    address, ex.getMessage());
            return "cart";
        }

        model.addAttribute("order", OrderResponseDTO.fromEntity(completedOrder));
        model.addAttribute("appliedDiscount", coupon != null ? coupon.calculateDiscount(completedOrder) : 0.0);
        session.removeAttribute("CART");
        return "order-success";
    }

    private void repopulateCart(Model model, CustomerDTO loggedInUser, List<OrderItem> cart,
                                Order previewOrder, String couponCode, String selectedAddressId,
                                String enteredAddress, String errorMessage) {
        List<CartItemDTO> cartItemDTOs = cart.stream().map(CartItemDTO::fromEntity).collect(Collectors.toList());
        model.addAttribute("user", loggedInUser);
        model.addAttribute("cartItems", cartItemDTOs);
        model.addAttribute("cartTotal", cartService.calculateTotal(cart));
        model.addAttribute("cartCount", cartService.calculateCount(cart));
        model.addAttribute("couponOptions", couponService.getCouponOptions(previewOrder));
        model.addAttribute("enteredCouponCode", couponCode);
        model.addAttribute("addressOptions", addressService.getAddressOptions(loggedInUser.getUserId()));
        model.addAttribute("selectedAddressId", selectedAddressId != null ? selectedAddressId : "");
        model.addAttribute("enteredAddress", enteredAddress != null ? enteredAddress : "");
        model.addAttribute("checkoutError", errorMessage);
    }
}
