package com.foodorder.service;

import com.foodorder.builder.DeliveryOrderBuilder;
import com.foodorder.builder.IOrderBuilder;
import com.foodorder.builder.OrderDirector;
import com.foodorder.entity.OrderRecordEntity;
import com.foodorder.model.Coupon;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.model.Payment;
import com.foodorder.model.enums.OrderStatus;
import com.foodorder.model.enums.PaymentMethod;
import com.foodorder.model.enums.PaymentStatus;
import com.foodorder.repository.OrderJpaRepository;
import com.foodorder.strategy.PaymentStrategy;
import com.foodorder.strategy.PaymentStrategyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderJpaRepository orderRepository;
    private final PaymentStrategyResolver paymentStrategyResolver;

    @Autowired
    public OrderService(OrderJpaRepository orderRepository, PaymentStrategyResolver paymentStrategyResolver) {
        this.orderRepository = orderRepository;
        this.paymentStrategyResolver = paymentStrategyResolver;
    }

    /**
     * Dùng Builder và Director để tạo Order phức tạp.
     * Theo chuẩn Builder Pattern:
     * - Director điều phối quá trình build (void)
     * - Client lấy kết quả từ Builder qua getResult()
     * - Coupon được áp dụng riêng qua Order.applyCoupon()
     */
    public Order createDeliveryOrder(Customer customer, List<OrderItem> items, String address, Coupon coupon,
                                     PaymentMethod paymentMethod) {
        validateCheckoutInput(customer, items, address);

        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.COD;
        }

        Order newOrder = buildOrder(customer, items, address);
        if (coupon != null) {
            newOrder.applyCoupon(coupon);
        }

        newOrder.setOrderId(UUID.randomUUID().toString());
        attachPayment(newOrder, paymentMethod);
        processPayment(newOrder.getPayment());

        saveOrderSnapshot(newOrder);
        return newOrder;
    }

    /**
     * Lấy danh sách tất cả đơn hàng từ CSDL
     */
    public List<Order> getAllOrders() {
        List<OrderRecordEntity> records = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        for (OrderRecordEntity record : records) {
            orders.add(mapToDomain(record));
        }
        return orders;
    }

    public Order getOrderById(String orderId) {
        OrderRecordEntity record = findRecordById(orderId);
        return mapToDomain(record);
    }

    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ.");
        }
        OrderRecordEntity record = findRecordById(orderId);
        record.setOrderStatus(newStatus);
        orderRepository.save(record);
        return mapToDomain(record);
    }

    public Order updatePaymentStatus(String orderId, PaymentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ.");
        }
        OrderRecordEntity record = findRecordById(orderId);
        record.setPaymentStatus(newStatus);
        if (newStatus == PaymentStatus.COMPLETED) {
            record.setPaidAt(LocalDateTime.now());
        }
        orderRepository.save(record);
        return mapToDomain(record);
    }

    private Order buildOrder(Customer customer, List<OrderItem> items, String address) {
        IOrderBuilder builder = new DeliveryOrderBuilder();
        OrderDirector director = new OrderDirector(builder);
        director.constructOnlineOrder(
                customer,
                items,
                null,
                address,
                LocalDateTime.now().plusMinutes(30)
        );
        return builder.getResult();
    }

    private void attachPayment(Order order, PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8));
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(order.calculateTotal());
        payment.setOrder(order);
        payment.setPaymentStrategy(paymentStrategyResolver.resolve(paymentMethod));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        order.setPayment(payment);
    }

    /**
     * Rule trạng thái demo:
     * - COD: tạo đơn thành công => Payment PENDING (thu tiền lúc giao).
     * - BANKING: xử lý ngay qua strategy => COMPLETED / FAILED.
     */
    private void processPayment(Payment payment) {
        PaymentStrategy strategy = payment.getPaymentStrategy();
        if (strategy == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            return;
        }

        if (payment.getPaymentMethod() == PaymentMethod.COD) {
            boolean accepted = strategy.processPayment(payment.getAmount(), payment.getPaymentId());
            payment.setPaymentStatus(accepted ? PaymentStatus.PENDING : PaymentStatus.FAILED);
            return;
        }

        payment.processTransaction();
    }

    private void validateCheckoutInput(Customer customer, List<OrderItem> items, String address) {
        if (customer == null || customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Tên khách hàng không hợp lệ.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ giao hàng không được để trống.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống.");
        }

        for (OrderItem item : items) {
            if (item == null || item.getQuantity() <= 0 || item.getDish() == null) {
                throw new IllegalArgumentException("Dữ liệu món trong giỏ hàng không hợp lệ.");
            }
        }
    }

    private void saveOrderSnapshot(Order order) {
        OrderRecordEntity entity = new OrderRecordEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerName(order.getCustomer() != null ? order.getCustomer().getFullName() : "Unknown");
        entity.setShippingAddress(order.getShippingAddress());
        entity.setSubTotal(order.getSubTotal());
        entity.setShippingFee(order.getShippingFee());
        entity.setTotalAmount(order.calculateTotal());
        entity.setOrderStatus(order.getStatus());
        entity.setOrderTime(order.getOrderTime());
        entity.setEstimatedPickupTime(order.getEstimatedPickupTime());

        Payment payment = order.getPayment();
        if (payment != null) {
            entity.setPaymentId(payment.getPaymentId());
            entity.setPaymentMethod(payment.getPaymentMethod());
            entity.setPaymentStatus(payment.getPaymentStatus());
            entity.setPaidAt(payment.getPaidAt());
        }

        orderRepository.save(entity);
    }

    private OrderRecordEntity findRecordById(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ.");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng: " + orderId));
    }

    private Order mapToDomain(OrderRecordEntity record) {
        Order order = new Order();
        order.setOrderId(record.getOrderId());
        order.setOrderTime(record.getOrderTime());
        order.setSubTotal(record.getSubTotal());
        order.setShippingFee(record.getShippingFee());
        order.setShippingAddress(record.getShippingAddress());
        order.setEstimatedPickupTime(record.getEstimatedPickupTime());
        order.setStatus(record.getOrderStatus() != null ? record.getOrderStatus() : OrderStatus.RECEIVED);

        Customer customer = new Customer();
        customer.setFullName(record.getCustomerName());
        order.setCustomer(customer);

        Payment payment = new Payment();
        payment.setPaymentId(record.getPaymentId());
        payment.setPaymentMethod(record.getPaymentMethod());
        payment.setPaymentStatus(record.getPaymentStatus());
        payment.setPaidAt(record.getPaidAt());
        payment.setAmount(record.getTotalAmount());
        order.setPayment(payment);

        return order;
    }
}
