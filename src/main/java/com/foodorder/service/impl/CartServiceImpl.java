package com.foodorder.service.impl;

import com.foodorder.decorator.BaseDish;
import com.foodorder.decorator.IDish;
import com.foodorder.decorator.Topping;
import com.foodorder.entity.Dish;
import com.foodorder.entity.OrderItem;
import com.foodorder.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public Dish createDishWithToppings(String baseDishStr, List<String> toppings, String imageUrl) {
        String[] dishParts = baseDishStr.split("_");
        String dishName = dishParts[0];
        double dishPrice = Double.parseDouble(dishParts[1]);

        // Decorator Pattern: BaseDish là component gốc
        IDish finalDish = new BaseDish("D-" + System.currentTimeMillis(), dishName, dishPrice, imageUrl, "");

        // Bọc thêm Topping (Decorator) nếu có
        if (toppings != null && !toppings.isEmpty()) {
            for (String topStr : toppings) {
                String[] topParts = topStr.split("_");
                String topName = topParts[0];
                double topPrice = Double.parseDouble(topParts[1]);
                finalDish = new Topping(finalDish, topName, topPrice);
            }
        }

        return Dish.builder()
                .setDishId("D-" + System.currentTimeMillis())
                .setName(finalDish.getName())
                .setPrice(finalDish.getPrice())
                .setImageUrl(imageUrl)
                .setDescription("")
                .build();
    }

    @Override
    public void addItemToCart(List<OrderItem> cart, Dish dish, int quantity) {
        // Gộp nhóm: nếu đã có món cùng tên + giá → tăng quantity
        for (OrderItem item : cart) {
            if (item.getDish().getName().equals(dish.getName())
                    && item.getDish().getPrice() == dish.getPrice()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // Chưa có → tạo OrderItem mới
        OrderItem newItem = new OrderItem();
        newItem.setOrderItemId("OI-" + System.currentTimeMillis());
        newItem.setDish(dish);
        newItem.setQuantity(quantity);
        cart.add(newItem);
    }

    @Override
    public boolean removeItem(List<OrderItem> cart, String orderItemId) {
        return cart.removeIf(item -> item.getOrderItemId().equals(orderItemId));
    }

    @Override
    public OrderItem findItem(List<OrderItem> cart, String orderItemId) {
        for (OrderItem item : cart) {
            if (item.getOrderItemId().equals(orderItemId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void increaseQuantity(OrderItem item) {
        item.setQuantity(item.getQuantity() + 1);
    }

    @Override
    public boolean decreaseQuantity(List<OrderItem> cart, OrderItem item) {
        item.setQuantity(item.getQuantity() - 1);
        if (item.getQuantity() <= 0) {
            cart.remove(item);
            return true; // Món đã bị xóa
        }
        return false; // Món vẫn còn
    }

    @Override
    public double calculateTotal(List<OrderItem> cart) {
        return cart.stream().mapToDouble(OrderItem::calculateSubTotal).sum();
    }

    @Override
    public int calculateCount(List<OrderItem> cart) {
        return cart.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
