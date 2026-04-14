package com.foodorder.dto;

import com.foodorder.model.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemDTO {
    private String orderItemId;
    private String dishName;
    private double unitPrice;
    private int quantity;
    private double subTotal;

    public static CartItemDTO fromDomain(OrderItem orderItem) {
        if (orderItem == null) return null;
        CartItemDTO dto = new CartItemDTO();
        dto.setOrderItemId(orderItem.getOrderItemId());
        
        if (orderItem.getDish() != null) {
            dto.setDishName(orderItem.getDish().getName());
        } else {
            dto.setDishName("Món không xác định");
        }
        
        dto.setUnitPrice(orderItem.getUnitPriceAtPurchase());
        dto.setQuantity(orderItem.getQuantity());
        dto.setSubTotal(orderItem.calculateSubTotal());
        return dto;
    }
}
