package com.foodorder;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
    private String orderItemId;
    private Order order;
    private Dish dish;
    private List<Topping> toppings;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
        this.toppings = new ArrayList<>();
    }

    public OrderItem(String orderItemId, Order order, Dish dish, List<Topping> toppings, int quantity, double unitPrice) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.dish = dish;
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
