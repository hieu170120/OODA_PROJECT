package com.foodorder.uml.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "uml_customers")
public class Customer extends User {
    public Customer() {
    }

    public Customer(String id, String username, String password, String phone, String fullName) {
        super(id, username, password, phone, fullName);
    }

    public List<Dish> viewMenu(List<Dish> dishes) {
        return dishes;
    }

    public CartItem addToCart(Dish dish, int quantity) {
        if (dish == null || quantity <= 0) {
            throw new IllegalArgumentException("Dish and quantity must be valid.");
        }
        return new CartItem(null, quantity);
    }
}
