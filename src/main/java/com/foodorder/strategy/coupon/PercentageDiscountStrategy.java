package com.foodorder.strategy.coupon;

import com.foodorder.model.Order;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private double percent;
    private double maxDiscount;

    public PercentageDiscountStrategy() {
    }

    public PercentageDiscountStrategy(double percent, double maxDiscount) {
        this.percent = percent;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public double calculateDiscount(Order order) {
        if (order == null || percent <= 0) {
            return 0;
        }

        double discount = order.calculateSubtotalAmount() * (percent / 100.0);
        if (maxDiscount > 0 && discount > maxDiscount) {
            discount = maxDiscount;
        }
        return Math.max(discount, 0);
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
}
