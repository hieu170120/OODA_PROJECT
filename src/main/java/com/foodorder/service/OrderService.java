package com.foodorder.service;

import com.foodorder.builder.DeliveryOrderBuilder;
import com.foodorder.builder.IOrderBuilder;
import com.foodorder.builder.OrderDirector;
import com.foodorder.model.Coupon;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.Payment;
import com.foodorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Dùng Builder và Director để tạo Order phức tạp.
     * Theo chuẩn Builder Pattern:
     * - Director điều phối quá trình build (void)
     * - Client lấy kết quả từ Builder qua getResult()
     * - Coupon được áp dụng riêng qua Order.applyCoupon()
     */
    public Order createDeliveryOrder(Customer customer, List<OrderItem> items, String address, Coupon coupon) {
        // 1. Khởi tạo Builder (Loại đơn hàng giao tận nơi)
        IOrderBuilder builder = new DeliveryOrderBuilder();
        
        // 2. Khởi tạo Director (Điều phối viên)
        OrderDirector director = new OrderDirector(builder);
        
        // Tạm thời tạo payment rỗng (Thực tế sẽ lấy thông tin thanh toán từ User)
        Payment payment = new Payment(); 
        
        // 3. Director điều phối quá trình lắp ráp (void - đúng chuẩn Builder Pattern)
        director.constructOnlineOrder(
                customer, 
                items, 
                payment, 
                address, 
                LocalDateTime.now().plusMinutes(30)
        );

        // 4. Client lấy kết quả từ Builder
        Order newOrder = builder.getResult();

        // 5. Áp dụng coupon riêng biệt thông qua Order.applyCoupon() (đúng class diagram)
        if (coupon != null) {
            newOrder.applyCoupon(coupon);
        }

        // Sinh ID tự động cho đơn hàng
        newOrder.setOrderId(UUID.randomUUID().toString());

        // 6. Lưu vào DB qua Repository
        return orderRepository.save(newOrder); 
    }

    /**
     * Lấy danh sách tất cả đơn hàng từ CSDL
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
