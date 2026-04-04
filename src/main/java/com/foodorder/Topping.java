package com.foodorder;

public class Topping {
    private String toppingId;
    private String name;
    private double price;

    public Topping() {
    }

    public Topping(String toppingId, String name, double price) {
        this.toppingId = toppingId;
        this.name = name;
        this.price = price;
    }

    public String getToppingId() {
        return toppingId;
    }

    public void setToppingId(String toppingId) {
        this.toppingId = toppingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
