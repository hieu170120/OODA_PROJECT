package com.foodorder.controller;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
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
        
        // 1. Tạo Khách hàng
        Customer customer = new Customer();
        customer.setUserId("CUST-999");
        customer.setFullName("Nguyen Van Demo");

        // 2. Demo Decorator Pattern: Tạo món ăn và thêm Topping
        
        // Món 1: Gà rán nguyên bản (BaseDish)
        IDish friedChicken = new BaseDish("D1", "Gà rán giòn", 35000.0, "img/chicken.png", "Gà rán KFC");
        
        // Món 2: Burger (BaseDish) + Thêm Phô mai (Topping 1) + Thêm Trứng (Topping 2)
        IDish burger = new BaseDish("D2", "Burger Bò", 45000.0, "img/burger.png", "Burger bò phô mai");
        burger = new Topping(burger, "Phô mai", 10000.0); // Bọc lần 1
        burger = new Topping(burger, "Trứng ốp la", 8000.0); // Bọc lần 2

        // 3. Đưa các món ăn vào OrderItem
        List<OrderItem> items = new ArrayList<>();
        
        // Khách mua 2 miếng gà rán (35k x 2 = 70k)
        OrderItem item1 = new OrderItem("OI-1", null, friedChicken, 2, friedChicken.getPrice());
        
        // Khách mua 1 Burger đầy đủ topping (45k + 10k + 8k = 63k)
        OrderItem item2 = new OrderItem("OI-2", null, burger, 1, burger.getPrice());
        
        items.add(item1);
        items.add(item2);

        // 4. Demo tạo Order bằng Builder Pattern thông qua Service
        String deliveryAddress = "Đại học Tôn Đức Thắng, TP.HCM";
        Coupon noCoupon = null; // Không dùng mã giảm giá

        // Gọi Service để tạo đơn hàng. Service sẽ dùng OrderDirector và DeliveryOrderBuilder
        Order createdOrder = orderService.createDeliveryOrder(
                customer, items, deliveryAddress, noCoupon, PaymentMethod.COD
        );

        // 5. Tính tổng tiền để chứng minh Decorator hoạt động đúng
        // Tiền hàng: 70k + 63k = 133k
        // Phí ship (đã cấu hình trong Builder): 15k
        // Tổng cộng (Total): 148k
        double calculatedTotal = createdOrder.calculateTotal();
        
        // In ra console để xem
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
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
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
            return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
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
            return ResponseEntity.ok(orderService.updatePaymentStatus(orderId, status));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
