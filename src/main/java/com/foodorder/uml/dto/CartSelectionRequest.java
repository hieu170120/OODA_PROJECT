package com.foodorder.uml.dto;

import java.util.List;

public class CartSelectionRequest {
    private String cartItemId;
    private String dishId;
    private List<String> toppingIds;

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public List<String> getToppingIds() {
        return toppingIds;
    }

    public void setToppingIds(List<String> toppingIds) {
        this.toppingIds = toppingIds;
    }
}
