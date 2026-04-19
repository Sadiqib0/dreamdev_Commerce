package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.AddToCartRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.CartResponse;

public interface CartService {
    CartResponse addToCart(AddToCartRequest request);
    CartResponse getCart(Long customerId);
    CartResponse removeFromCart(Long cartItemId);
    void clearCart(Long customerId);
}
