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

    // /**
    //  * Dùng Builder và Director để tạo Order phức tạp.
    //  * Theo chuẩn Builder Pattern:
    //  * - Director điều phối quá trình build (void)
    //  * - Client lấy kết quả từ Builder qua getResult()
    //  * - Coupon được áp dụng riêng qua Order.applyCoupon()
    //  *
    //  * @param paymentMethodCode mã phương thức (khớp {@link PaymentStrategy#getMethodCode()}), null/rỗng → COD.
    //  */
    public Order createDeliveryOrder(Customer customer, List<OrderItem> items, String address, Coupon coupon,
                                     String paymentMethodCode) {
        validateCheckoutInput(customer, items, address);

        PaymentMethod method = PaymentMethod.COD;
        if (paymentMethodCode != null && !paymentMethodCode.trim().isEmpty()) {
            try {
                method = PaymentMethod.valueOf(paymentMethodCode.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Phương thức thanh toán không hỗ trợ: " + paymentMethodCode);
            }
        }

        if (paymentStrategyResolver.resolve(method) == null) {
            throw new IllegalArgumentException("Phương thức thanh toán chưa được implement: " + method);
        }

        Order newOrder = buildOrder(customer, items, address);
        if (coupon != null) {
            newOrder.applyCoupon(coupon);
        }

        newOrder.setOrderId(UUID.randomUUID().toString());
        attachPayment(newOrder, method);
        processPayment(newOrder.getPayment());

        saveOrderSnapshot(newOrder);
        return newOrder;
    }

    /**
     * Lấy danh sách tất cả đơn hàng từ CSDL
     */
    public List<Order> getAllOrders() {
        List<OrderRecordEntity> records = orderRepository.findAllByOrderByOrderTimeDesc();
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
        OrderStatus current = record.getOrderStatus() != null ? record.getOrderStatus() : OrderStatus.RECEIVED;
        orderStateFactory.forStatus(current).validateTransition(newStatus);
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
     * - Deferred (VD COD): chỉ validate → PENDING.
     * - Khác: xử lý ngay qua strategy → COMPLETED / FAILED.
     */
    private void processPayment(Payment payment) {
        PaymentStrategy strategy = payment.getPaymentStrategy();
        if (strategy == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            return;
        }

        if (strategy.isDeferredPayment()) {
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
        // Lưu totalAmount đã tính sẵn (bao gồm coupon discount) từ DB
        order.setTotalAmount(record.getTotalAmount());
        order.setShippingAddress(record.getShippingAddress());
        order.setEstimatedPickupTime(record.getEstimatedPickupTime());
        order.setStatus(record.getOrderStatus() != null ? record.getOrderStatus() : OrderStatus.RECEIVED);

        Customer customer = new Customer();
        customer.setFullName(record.getCustomerName());
        order.setCustomer(customer);

        Payment payment = new Payment();
        payment.setPaymentId(record.getPaymentId());
        PaymentMethod method = record.getPaymentMethod();
        if (method == null) {
            method = PaymentMethod.COD;
        }
        payment.setPaymentMethod(method);
        payment.setPaymentStrategy(paymentStrategyResolver.resolve(method));
        payment.setPaymentStatus(record.getPaymentStatus());
        payment.setPaidAt(record.getPaidAt());
        payment.setAmount(record.getTotalAmount());
        order.setPayment(payment);

        return order;
    }
}
