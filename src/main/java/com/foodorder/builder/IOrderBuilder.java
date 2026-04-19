package com.foodorder.builder;

import com.foodorder.entity.Payment;
import com.foodorder.entity.Customer;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderBuilder {
    void reset();                                  
    void buildCustomer(Customer customer);          
    void buildItems(List<OrderItem> items);         
    void buildDeliveryInfo(String address,LocalDateTime pickupTime);
    void buildPayment(Payment payment);             
    Order getResult();                             
}

