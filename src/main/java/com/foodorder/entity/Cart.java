package com.foodorder.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")



public class Cart {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "customer_id", nullable = false, length = 64)
    private String customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItem = new ArrayList<>();

    public void checkout() {
    }

    public double calculateSubTotal(double price) {
        return cartItem.stream()
                .mapToDouble(item -> item.getQuantity() * price)
                .sum();
    }

    public Cart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }
}
