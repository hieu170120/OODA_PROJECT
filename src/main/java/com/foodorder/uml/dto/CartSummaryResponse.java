package com.foodorder.uml.dto;

import com.foodorder.uml.entity.Cart;

import java.util.List;

public class CartSummaryResponse {
    private final Cart cart;
    private final List<CartSelectionRequest> selections;
    private final double subtotal;

    public CartSummaryResponse(Cart cart, List<CartSelectionRequest> selections, double subtotal) {
        this.cart = cart;
        this.selections = selections;
        this.subtotal = subtotal;
    }

    public Cart getCart() {
        return cart;
    }

    public List<CartSelectionRequest> getSelections() {
        return selections;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
