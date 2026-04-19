package org.dreamcommerce.dreamCommerce.controller;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.AddToCartRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.CartResponse;
import org.dreamcommerce.dreamCommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(cartItemId));
    }

    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
