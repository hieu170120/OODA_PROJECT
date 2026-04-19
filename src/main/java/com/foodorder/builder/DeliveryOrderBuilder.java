package com.foodorder.builder;

import com.foodorder.entity.Payment;
import com.foodorder.entity.Customer;
import com.foodorder.entity.Order;
import com.foodorder.entity.OrderItem;
import com.foodorder.enums.OrderStatus;

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
        for (OrderItem item : items) {
            item.setOrder(this.result);
        }
    }

    @Override
    public void buildDeliveryInfo(String address, LocalDateTime pickupTime) {
    }

    @Override
    public void buildPayment(Payment payment) {
        if (payment != null) {
            this.result.setPayment(payment);            
        }
    }

    @Override
    public Order getResult() {
        Order finalOrder = this.result;                 
        this.reset();                                   
        return finalOrder;                              
    }
}
