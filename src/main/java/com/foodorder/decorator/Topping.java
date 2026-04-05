package com.foodorder.decorator;

public class Topping extends DishDecorator {
    private String toppingId;
    private String name;
    private double price;

    public Topping(IDish dish, String name, double price) {
        super(dish);
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return super.getName() + " + " + name;
    }

    @Override
    public double getPrice() {
        return super.getPrice() + price;
    }

    public String getToppingId() {
        return toppingId;
    }

    public void setToppingId(String toppingId) {
        this.toppingId = toppingId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
