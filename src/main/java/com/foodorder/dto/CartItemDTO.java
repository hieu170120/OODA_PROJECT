package com.foodorder.dto;

import com.foodorder.entity.OrderItem;

public class CartItemDTO {
    private String orderItemId;
    private String dishName;
    private double unitPrice;
    private int quantity;
    private double subTotal;

    public CartItemDTO() {
    }

    public static CartItemDTO fromEntity(OrderItem orderItem) {
        if (orderItem == null) return null;
        CartItemDTO dto = new CartItemDTO();
        dto.setOrderItemId(orderItem.getOrderItemId());
        
        if (orderItem.getDish() != null) {
            dto.setDishName(orderItem.getDish().getName());
        } else {
            dto.setDishName("Món không xác định");
        }
        
        dto.setUnitPrice(orderItem.getDish() != null && orderItem.getDish().getPrice() != null
                ? orderItem.getDish().getPrice()
                : 0.0);
        dto.setQuantity(orderItem.getQuantity());
        dto.setSubTotal(orderItem.calculateSubTotal());
        return dto;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
