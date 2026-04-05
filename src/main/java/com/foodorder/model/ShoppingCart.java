package com.foodorder.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private String cartId;
    private Customer customer;
    private List<CartItem> cartItems;

    public ShoppingCart() {
        this.cartItems = new ArrayList<>();
    }

    public ShoppingCart(String cartId, Customer customer) {
        this.cartId = cartId;
        this.customer = customer;
        this.cartItems = new ArrayList<>();
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem item) {
        if (item != null) {
            this.cartItems.add(item);
        }
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
