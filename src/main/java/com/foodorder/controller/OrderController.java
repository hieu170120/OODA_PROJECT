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
import com.foodorder.model.enums.PaymentMethod;
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
     * API xem tất cả đơn hàng hiện có
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        // Ánh xạ Domain Model sang DTO
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
        } catch (IllegalArgumentException ex) {
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
