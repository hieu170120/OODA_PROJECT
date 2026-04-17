package com.foodorder.uml.controller;

import com.foodorder.uml.dto.CartSelectionRequest;
import com.foodorder.uml.dto.CartSummaryResponse;
import com.foodorder.uml.entity.Cart;
import com.foodorder.uml.service.CartApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uml/carts")
public class UmlCartController {
    private final CartApplicationService cartApplicationService;

    public UmlCartController(CartApplicationService cartApplicationService) {
        this.cartApplicationService = cartApplicationService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartApplicationService.createCart(cart));
    }

    @PostMapping("/{cartId}/summary")
    public CartSummaryResponse summarizeCart(@PathVariable String cartId,
                                             @RequestBody List<CartSelectionRequest> selections) {
        return cartApplicationService.summarizeCart(cartId, selections);
    }
}
