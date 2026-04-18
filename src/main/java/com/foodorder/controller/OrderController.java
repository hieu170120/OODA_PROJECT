package com.foodorder.controller;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.model.Coupon;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.enums.OrderStatus;
import com.foodorder.model.enums.PaymentStatus;
import com.foodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * API tạo đơn hàng - Demo kết hợp Decorator Pattern và Builder Pattern
     * Có thể test thử bằng cách truy cập: http://localhost:8080/api/v1/orders/checkout/demo
     */
    @PostMapping("/checkout/demo")
    public ResponseEntity<Order> checkoutDemo() {

        Customer customer = new Customer();
        customer.setUserId("CUST-999");
        customer.setFullName("Nguyen Van Demo");

        IDish friedChicken = new BaseDish("D1", "Gà rán giòn", 35000.0, "img/chicken.png", "Gà rán KFC");

        IDish burger = new BaseDish("D2", "Burger Bò", 45000.0, "img/burger.png", "Burger bò phô mai");
        burger = new Topping(burger, "Phô mai", 10000.0);
        burger = new Topping(burger, "Trứng ốp la", 8000.0);

        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem("OI-1", null, friedChicken, 2, friedChicken.getPrice());
        OrderItem item2 = new OrderItem("OI-2", null, burger, 1, burger.getPrice());
        items.add(item1);
        items.add(item2);

        String deliveryAddress = "Đại học Tôn Đức Thắng, TP.HCM";
        Order createdOrder = orderService.createDeliveryOrder(
            customer, items, deliveryAddress, null, 0, "COD"
        );

        double calculatedTotal = createdOrder.calculateTotal();
        System.out.println("=====================================");
        System.out.println("TEST DEMO DECORATOR & BUILDER PATTERN");
        System.out.println("Tên món 1: " + friedChicken.getName() + " | Giá: " + friedChicken.getPrice());
        System.out.println("Tên món 2: " + burger.getName() + " | Giá: " + burger.getPrice());
        System.out.println("Tổng hóa đơn (kèm ship): " + calculatedTotal);
        System.out.println("=====================================");

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * API xem tất cả đơn hàng hiện có
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Admin/demo: cập nhật trạng thái đơn hàng.
     * Ví dụ: PATCH /api/v1/orders/{id}/status?status=PREPARING
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId,
                                               @RequestParam("status") OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Admin/demo: cập nhật trạng thái thanh toán.
     * Ví dụ: PATCH /api/v1/orders/{id}/payment-status?status=COMPLETED
     */
    @PatchMapping("/{orderId}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String orderId,
                                                 @RequestParam("status") PaymentStatus status) {
        try {
            Order updatedOrder = orderService.updatePaymentStatus(orderId, status);
            return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
