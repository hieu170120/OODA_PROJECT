package com.foodorder.controller;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
import com.foodorder.dto.OrderResponseDTO;
import com.foodorder.entity.Customer;
import com.foodorder.entity.Dish;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.enums.OrderStatus;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/checkout/demo")
    public ResponseEntity<Order> checkoutDemo() {
        Customer customer = new Customer();
        customer.setUserId("CUST-999");
        customer.setFullName("Nguyen Van Demo");

        IDish friedChicken = new BaseDish("D1", "Ga ran gion", 35000.0, "img/chicken.png", "Ga ran KFC");
        IDish burger = new BaseDish("D2", "Burger Bo", 45000.0, "img/burger.png", "Burger bo pho mai");
        burger = new Topping(burger, "Pho mai", 10000.0);
        burger = new Topping(burger, "Trung op la", 8000.0);

        List<OrderItem> items = new ArrayList<>();
        items.add(createOrderItem("OI-1", friedChicken, 2));
        items.add(createOrderItem("OI-2", burger, 1));

        Order createdOrder = orderService.createDeliveryOrder(
                customer, items, "Dai hoc Ton Duc Thang, TP.HCM", null, "COD"
        );

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> response = orderService.getAllOrders().stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(orderService.getOrderById(orderId)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId,
                                               @RequestParam("status") OrderStatus status) {
        try {
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(orderService.updateOrderStatus(orderId, status)));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("/{orderId}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String orderId,
                                                 @RequestParam("status") PaymentStatus status) {
        try {
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(orderService.updatePaymentStatus(orderId, status)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    private OrderItem createOrderItem(String id, IDish sourceDish, int quantity) {
        Dish dish = Dish.builder()
                .setDishId(id.replace("OI", "D"))
                .setName(sourceDish.getName())
                .setPrice(sourceDish.getPrice())
                .setImageUrl("")
                .setDescription("")
                .build();

        OrderItem item = new OrderItem();
        item.setOrderItemId(id);
        item.setDish(dish);
        item.setQuantity(quantity);
        return item;
    }
}
