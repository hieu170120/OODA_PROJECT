package com.foodorder.builder;

import com.foodorder.entity.Payment;
import com.foodorder.entity.Customer;
import com.foodorder.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDirector {
    private IOrderBuilder builder;

    public OrderDirector(IOrderBuilder builder) {
        this.builder = builder;
    }

    public void constructOnlineOrder(Customer customer, List<OrderItem> items,  Payment payment, String address,LocalDateTime pickupTime) {
        builder.reset();                                
        builder.buildCustomer(customer);               
        builder.buildItems(items);                     
        builder.buildDeliveryInfo(address, pickupTime); 
        builder.buildPayment(payment);                 
    }
}

