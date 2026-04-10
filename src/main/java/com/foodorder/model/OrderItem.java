package com.foodorder.model;

import com.foodorder.decorator.IDish;

public class OrderItem {
    private String orderItemId;
    private Order order;
    private IDish dish;
    private int quantity;
    private double unitPriceAtPurchase;

    public OrderItem() {
    }

    public OrderItem(String orderItemId, Order order, IDish dish, int quantity, double unitPriceAtPurchase) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.unitPriceAtPurchase = unitPriceAtPurchase;
    }

    public double calculateSubTotal() {
        return quantity * unitPriceAtPurchase;
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

    public IDish getDish() {
        return dish;
    }

    public void setDish(IDish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPriceAtPurchase() {
        return unitPriceAtPurchase;
    }

    public void setUnitPriceAtPurchase(double unitPriceAtPurchase) {
        this.unitPriceAtPurchase = unitPriceAtPurchase;
    }
}
