package com.foodorder.service;

import com.foodorder.decorator.IDish;
import com.foodorder.model.OrderItem;

import java.util.List;

public interface CartService {

    IDish createDishWithToppings(String baseDishStr, List<String> toppings, String imageUrl);

    void addItemToCart(List<OrderItem> cart, IDish dish, int quantity);

    boolean removeItem(List<OrderItem> cart, String orderItemId);

    OrderItem findItem(List<OrderItem> cart, String orderItemId);

    void increaseQuantity(OrderItem item);
    
    boolean decreaseQuantity(List<OrderItem> cart, OrderItem item);

    double calculateTotal(List<OrderItem> cart);

    int calculateCount(List<OrderItem> cart);
}
