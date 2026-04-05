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
     * Dùng Builder và Director để tạo Order phức tạp
     */
    public Order createDeliveryOrder(Customer customer, List<OrderItem> items, String address, Coupon coupon) {
        // 1. Khởi tạo Builder (Loại đơn hàng giao tận nơi)
        IOrderBuilder builder = new DeliveryOrderBuilder();
        
        // 2. Khởi tạo Director (Điều phối viên)
        OrderDirector director = new OrderDirector(builder);
        
        // Tạm thời tạo payment rỗng (Thực tế sẽ lấy thông tin thanh toán từ User)
        Payment payment = new Payment(); 
        
        // 3. Gọi Director để tự động hóa việc lắp ráp các thành phần của một đơn hàng Online
        // Truyền thời gian giao hàng dự kiến là 30 phút kể từ lúc đặt
        Order newOrder = director.constructOnlineOrder(
                customer, 
                items, 
                payment, 
                address, 
                LocalDateTime.now().plusMinutes(30), 
                coupon
        );

        // Sinh ID tự động cho đơn hàng
        newOrder.setOrderId(UUID.randomUUID().toString());

        // 4. Lưu vào DB qua Repository
        return orderRepository.save(newOrder); 
    }

    /**
     * Lấy danh sách tất cả đơn hàng từ CSDL
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
