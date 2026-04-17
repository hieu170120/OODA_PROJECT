package com.foodorder.uml.service;

import com.foodorder.uml.dto.CartSelectionRequest;
import com.foodorder.uml.dto.CartSummaryResponse;
import com.foodorder.uml.entity.Cart;
import com.foodorder.uml.entity.CartItem;
import com.foodorder.uml.entity.Dish;
import com.foodorder.uml.entity.Topping;
import com.foodorder.uml.repository.CartItemRepository;
import com.foodorder.uml.repository.CartRepository;
import com.foodorder.uml.repository.DishRepository;
import com.foodorder.uml.repository.ToppingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final ToppingRepository toppingRepository;

    public CartApplicationService(CartRepository cartRepository,
                                  CartItemRepository cartItemRepository,
                                  DishRepository dishRepository,
                                  ToppingRepository toppingRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = dishRepository;
        this.toppingRepository = toppingRepository;
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public CartSummaryResponse summarizeCart(String cartId, List<CartSelectionRequest> selections) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));

        double subtotal = 0;
        for (CartSelectionRequest selection : selections) {
            CartItem cartItem = cartItemRepository.findById(selection.getCartItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + selection.getCartItemId()));
            Dish dish = dishRepository.findById(selection.getDishId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found: " + selection.getDishId()));

            double lineTotal = cartItem.getQuantity() * dish.getPrice();
            for (Topping topping : loadToppings(selection.getToppingIds())) {
                lineTotal += cartItem.getQuantity() * topping.getPrice();
            }
            subtotal += lineTotal;
        }
        return new CartSummaryResponse(cart, selections, subtotal);
    }

    private List<Topping> loadToppings(List<String> toppingIds) {
        List<Topping> toppings = new ArrayList<>();
        if (toppingIds == null) {
            return toppings;
        }
        for (String toppingId : toppingIds) {
            toppings.add(toppingRepository.findById(toppingId)
                    .orElseThrow(() -> new IllegalArgumentException("Topping not found: " + toppingId)));
        }
        return toppings;
    }
}
