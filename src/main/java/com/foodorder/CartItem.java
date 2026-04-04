package com.foodorder;

public class CartItem {
    private String cartItemId;
    private ShoppingCart shoppingCart;
    private Dish dish;
    private int quantity;
    private double unitPrice;

    public CartItem() {
    }

    public CartItem(String cartItemId, ShoppingCart shoppingCart, Dish dish, int quantity, double unitPrice) {
        this.cartItemId = cartItemId;
        this.shoppingCart = shoppingCart;
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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
