package com.foodorder.uml.controller;

import com.foodorder.uml.dto.CheckoutRequest;
import com.foodorder.uml.dto.CheckoutResponse;
import com.foodorder.uml.entity.Order;
import com.foodorder.uml.entity.Payment;
import com.foodorder.uml.enums.OrderStatus;
import com.foodorder.uml.service.CheckoutApplicationService;
import com.foodorder.uml.service.OrderManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uml/orders")
public class UmlOrderController {
    private final OrderManagementService orderManagementService;
    private final CheckoutApplicationService checkoutApplicationService;

    public UmlOrderController(OrderManagementService orderManagementService,
                              CheckoutApplicationService checkoutApplicationService) {
        this.orderManagementService = orderManagementService;
        this.checkoutApplicationService = checkoutApplicationService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderManagementService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return orderManagementService.getOrderById(orderId);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderManagementService.createOrder(order));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable String orderId,
                                                  @RequestParam("status") OrderStatus status) {
        orderManagementService.updateOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public CheckoutResponse checkout(@RequestBody CheckoutRequest request) {
        return checkoutApplicationService.checkout(request);
    }

    @PatchMapping("/payments/{paymentId}/process")
    public Payment processPayment(@PathVariable String paymentId) {
        return checkoutApplicationService.processPayment(paymentId);
    }
}
