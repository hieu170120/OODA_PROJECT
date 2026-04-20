package com.foodorder.service;

import com.foodorder.builder.DeliveryOrderBuilder;
import com.foodorder.builder.IOrderBuilder;
import com.foodorder.builder.OrderDirector;
import com.foodorder.entity.Customer;
import com.foodorder.entity.Coupon;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.entity.OrderRecordEntity;
import com.foodorder.entity.Payment;
import com.foodorder.enums.OrderStatus;
import com.foodorder.enums.PaymentMethod;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.repository.OrderJpaRepository;
import com.foodorder.state.order.OrderStateFactory;
import com.foodorder.strategy.payment.PaymentStrategy;
import com.foodorder.strategy.payment.PaymentStrategyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderJpaRepository orderRepository;
    private final PaymentStrategyResolver paymentStrategyResolver;
    private final OrderStateFactory orderStateFactory;

    @Autowired
    public OrderService(OrderJpaRepository orderRepository,
                        PaymentStrategyResolver paymentStrategyResolver,
                        OrderStateFactory orderStateFactory) {
        this.orderRepository = orderRepository;
        this.paymentStrategyResolver = paymentStrategyResolver;
        this.orderStateFactory = orderStateFactory;
    }

    public Map<OrderStatus, Set<OrderStatus>> getOrderStatusSelectOptions() {
        return orderStateFactory.selectableGrouped();
    }

    public Order createDeliveryOrder(Customer customer, List<OrderItem> items, String address, Coupon coupon,
                                     String paymentMethodCode) {
        validateCheckoutInput(customer, items, address);

        PaymentMethod method = resolvePaymentMethod(paymentMethodCode);
        Order newOrder = buildOrder(customer, items);
        newOrder.setOrderId(UUID.randomUUID().toString());

        Payment payment = attachPayment(newOrder, method, coupon);
        processPayment(payment);
        saveOrderSnapshot(newOrder, address, payment);
        return newOrder;
    }

    public List<Order> getAllOrders() {
        List<OrderRecordEntity> records = orderRepository.findAllByOrderByOrderTimeDesc();
        List<Order> orders = new ArrayList<>();
        for (OrderRecordEntity record : records) {
            orders.add(mapToEntity(record));
        }
        return orders;
    }

    public List<OrderRecordEntity> getAllOrderRecords() {
        return orderRepository.findAllByOrderByOrderTimeDesc();
    }

    public Order getOrderById(String orderId) {
        return mapToEntity(findRecordById(orderId));
    }

    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Trang thai don hang khong hop le.");
        }

        OrderRecordEntity record = findRecordById(orderId);
        OrderStatus current = record.getOrderStatus() != null ? record.getOrderStatus() : OrderStatus.RECEIVED;
        orderStateFactory.forStatus(current).validateTransition(newStatus);
        record.setOrderStatus(newStatus);
        orderRepository.save(record);
        return mapToEntity(record);
    }

    public Order updatePaymentStatus(String orderId, PaymentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Trang thai thanh toan khong hop le.");
        }

        OrderRecordEntity record = findRecordById(orderId);
        record.setPaymentStatus(newStatus);
        if (newStatus == PaymentStatus.COMPLETED) {
            record.setPaidAt(LocalDateTime.now());
        }
        orderRepository.save(record);
        return mapToEntity(record);
    }

    private PaymentMethod resolvePaymentMethod(String paymentMethodCode) {
        PaymentMethod method = PaymentMethod.COD;
        if (paymentMethodCode != null && !paymentMethodCode.isBlank()) {
            try {
                method = PaymentMethod.valueOf(paymentMethodCode.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Phuong thuc thanh toan khong ho tro: " + paymentMethodCode);
            }
        }

        if (paymentStrategyResolver.resolve(method) == null) {
            throw new IllegalArgumentException("Phuong thuc thanh toan chua duoc implement: " + method);
        }
        return method;
    }

    private Order buildOrder(Customer customer, List<OrderItem> items) {
        IOrderBuilder builder = new DeliveryOrderBuilder();
        OrderDirector director = new OrderDirector(builder);
        director.constructOnlineOrder(customer, items, null, null, LocalDateTime.now().plusMinutes(30));
        return builder.getResult();
    }

    private Payment attachPayment(Order order, PaymentMethod paymentMethod, Coupon coupon) {
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8));

        double amount = order.calculateSubTotal();
        if (coupon != null) {
            amount = Math.max(0.0, amount - coupon.calculateDiscount(order));
        }
        payment.setAmount(amount);
        payment.setOrderId(order.getOrderId());
        
        order.setPaymentId(payment.getPaymentId());
        return payment;
    }

    private void processPayment(Payment payment) {
        PaymentStrategy strategy = paymentStrategyResolver.resolve(payment.getPaymentMethod());
        if (strategy == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            return;
        }

        if (strategy.isDeferredPayment()) {
            boolean accepted = strategy.processPayment(payment.getAmount(), payment.getPaymentId());
            payment.setPaymentStatus(accepted ? PaymentStatus.PENDING : PaymentStatus.FAILED);
            return;
        }

        boolean success = strategy.processPayment(payment.getAmount(), payment.getPaymentId());
        payment.setPaymentStatus(success ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        if (success) {
            payment.setPaidAt(LocalDateTime.now());
        }
    }

    private void validateCheckoutInput(Customer customer, List<OrderItem> items, String address) {
        if (customer == null || customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Ten khach hang khong hop le.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Dia chi giao hang khong duoc de trong.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Gio hang trong.");
        }
        for (OrderItem item : items) {
            if (item == null || item.getQuantity() == null || item.getQuantity() <= 0 || item.getDish() == null) {
                throw new IllegalArgumentException("Du lieu mon trong gio hang khong hop le.");
            }
        }
    }

    private void saveOrderSnapshot(Order order, String address, Payment payment) {
        OrderRecordEntity entity = new OrderRecordEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerName(order.getCustomer() != null ? order.getCustomer().getFullName() : "Unknown");
        entity.setShippingAddress(address);
        entity.setSubTotal(order.calculateSubTotal());
        entity.setShippingFee(0.0);
        entity.setTotalAmount(payment.getAmount());
        entity.setOrderStatus(order.getStatus());
        entity.setOrderTime(order.getOrderTime());
        entity.setEstimatedPickupTime(null);
        entity.setPaymentId(payment.getPaymentId());
        entity.setPaymentMethod(payment.getPaymentMethod());
        entity.setPaymentStatus(payment.getPaymentStatus());
        entity.setPaidAt(payment.getPaidAt());
        orderRepository.save(entity);
    }

    private OrderRecordEntity findRecordById(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Ma don hang khong hop le.");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay don hang: " + orderId));
    }

    private Order mapToEntity(OrderRecordEntity record) {
        Order order = new Order();
        order.setOrderId(record.getOrderId());
        order.setOrderTime(record.getOrderTime());
        order.setStatus(record.getOrderStatus() != null ? record.getOrderStatus() : OrderStatus.RECEIVED);

        Customer customer = new Customer();
        customer.setFullName(record.getCustomerName());
        order.setCustomer(customer);

        order.setPaymentId(record.getPaymentId());
        return order;
    }
}
