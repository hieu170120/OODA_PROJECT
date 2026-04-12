package com.foodorder.decorator;

public abstract class DishDecorator implements IDish {
    protected IDish wrappee;

    public DishDecorator(IDish dish) {
        this.wrappee = dish;
    }

    @Override
    public String getName() {
        return wrappee.getName();
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice();
    }

    @Override
    public String getImageUrl() {
        return wrappee.getImageUrl();
    }

    @Override
    public String getDescription() {
        return wrappee.getDescription();
    }
}
