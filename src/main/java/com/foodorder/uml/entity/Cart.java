package com.foodorder.uml.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "uml_carts")
public class Cart {
    @Id
    @Column(nullable = false, length = 64)
    private String id;

    public Cart() {
    }

    public Cart(String id) {
        this.id = id;
    }

    public Order checkout() {
        throw new UnsupportedOperationException("Checkout requires cart items managed outside the entity.");
    }

    public double calculateSubtotal() {
        throw new UnsupportedOperationException("Subtotal requires cart items managed outside the entity.");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
