package com.foodorder.builder;

import com.foodorder.model.Payment;
import com.foodorder.model.Customer;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderBuilder {
    void reset();
    void buildCustomer(Customer customer);
    void buildItems(List<OrderItem> items);
    void buildDeliveryInfo(String address, LocalDateTime pickupTime);
    void buildPayment(Payment payment);
    Order getResult();
}
