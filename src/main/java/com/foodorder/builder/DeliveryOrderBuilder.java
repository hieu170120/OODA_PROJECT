package com.foodorder.builder;

import com.foodorder.model.enums.OrderStatus;
import com.foodorder.model.Payment;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class DeliveryOrderBuilder implements IOrderBuilder {
    private Order result;

    public DeliveryOrderBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.result = new Order();
        this.result.setStatus(OrderStatus.RECEIVED);
        this.result.setOrderTime(LocalDateTime.now());
    }

    @Override
    public void buildCustomer(Customer customer) {
        this.result.setCustomer(customer);
    }

    @Override
    public void buildItems(List<OrderItem> items) {
        this.result.setOrderItems(items);
        double subTotal = 0;
        if (items != null) {
            for (OrderItem item : items) {
                subTotal += item.calculateSubTotal();
            }
        }
        this.result.setSubTotal(subTotal);
    }

    @Override
    public void buildDeliveryInfo(String address, LocalDateTime pickupTime) {
        this.result.setShippingAddress(address);
        this.result.setEstimatedPickupTime(pickupTime);
        // Có thể tính phí ship dựa trên khoảng cách, ở đây fix cứng 1 khoản
        this.result.setShippingFee(15000.0); 
    }

    @Override
    public void buildPayment(Payment payment) {
        this.result.setPayment(payment);
    }

    @Override
    public Order getResult() {
        Order finalOrder = this.result;
        this.reset(); // Sẵn sàng cho lần build tiếp theo
        return finalOrder;
    }
}
