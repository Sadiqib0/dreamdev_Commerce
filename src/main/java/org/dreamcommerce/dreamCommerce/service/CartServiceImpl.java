package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.AddToCartRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.CartResponse;
import org.dreamcommerce.dreamCommerce.models.Cart;
import org.dreamcommerce.dreamCommerce.models.CartItem;
import org.dreamcommerce.dreamCommerce.repository.CartItemRepository;
import org.dreamcommerce.dreamCommerce.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        ModelMapper mapper = new ModelMapper();

        Cart cart = cartRepository.findByCustomerId(request.getCustomerId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerId(request.getCustomerId());
                    return cartRepository.save(newCart);
                });

        cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setQuantity(existing.getQuantity() + request.getQuantity());
                            cartItemRepository.save(existing);
                        },
                        () -> {
                            CartItem item = new CartItem();
                            item.setCartId(cart.getId());
                            item.setProductId(request.getProductId());
                            item.setQuantity(request.getQuantity());
                            cartItemRepository.save(item);
                        }
                );

        return mapper.map(cart, CartResponse.class);
    }

    @Override
    public CartResponse getCart(Long customerId) {
        ModelMapper mapper = new ModelMapper();
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return mapper.map(cart, CartResponse.class);
    }

    @Override
    public CartResponse removeFromCart(Long cartItemId) {
        ModelMapper mapper = new ModelMapper();
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        Cart cart = cartRepository.findById(item.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteById(cartItemId);
        return mapper.map(cart, CartResponse.class);
    }

    @Override
    public void clearCart(Long customerId) {
        cartRepository.findByCustomerId(customerId).ifPresent(cart -> {
            cartItemRepository.findByCartId(cart.getId())
                    .forEach(item -> cartItemRepository.deleteById(item.getId()));
        });
    }
}
