package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.AddToCartRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.CartResponse;
import org.dreamcommerce.dreamCommerce.models.Cart;
import org.dreamcommerce.dreamCommerce.models.CartItem;
import org.dreamcommerce.dreamCommerce.repository.CartItemRepository;
import org.dreamcommerce.dreamCommerce.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void testAddToCartCreatesNewCartWhenNoneExists() {
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        Cart newCart = buildCart(10L, 1L);
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);
        when(cartItemRepository.findByCartIdAndProductId(10L, 5L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(new CartItem());

        AddToCartRequest request = new AddToCartRequest();
        request.setCustomerId(1L);
        request.setProductId(5L);
        request.setQuantity(2);

        CartResponse response = cartService.addToCart(request);

        assertNotNull(response);
        assertThat(response.getCustomerId()).isEqualTo(1L);
        verify(cartRepository).save(any(Cart.class));
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void testAddToCartMergesQuantityWhenItemAlreadyExists() {
        Cart existingCart = buildCart(10L, 1L);
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(existingCart));

        CartItem existingItem = buildCartItem(20L, 10L, 5L, 1);
        when(cartItemRepository.findByCartIdAndProductId(10L, 5L)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingItem);

        AddToCartRequest request = new AddToCartRequest();
        request.setCustomerId(1L);
        request.setProductId(5L);
        request.setQuantity(3);

        cartService.addToCart(request);

        assertThat(existingItem.getQuantity()).isEqualTo(4);
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    void testCanGetCart() {
        Cart cart = buildCart(10L, 1L);
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCart(1L);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void testGetCartThrowsWhenNotFound() {
        when(cartRepository.findByCustomerId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCart(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart not found");
    }

    @Test
    void testCanRemoveFromCart() {
        CartItem item = buildCartItem(20L, 10L, 5L, 1);
        Cart cart = buildCart(10L, 1L);
        when(cartItemRepository.findById(20L)).thenReturn(Optional.of(item));
        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));
        doNothing().when(cartItemRepository).deleteById(20L);

        CartResponse response = cartService.removeFromCart(20L);

        assertNotNull(response);
        verify(cartItemRepository).deleteById(20L);
    }

    @Test
    void testCanClearCart() {
        Cart cart = buildCart(10L, 1L);
        CartItem item1 = buildCartItem(20L, 10L, 5L, 1);
        CartItem item2 = buildCartItem(21L, 10L, 6L, 2);
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(10L)).thenReturn(List.of(item1, item2));
        doNothing().when(cartItemRepository).deleteById(anyLong());

        cartService.clearCart(1L);

        verify(cartItemRepository).deleteById(20L);
        verify(cartItemRepository).deleteById(21L);
    }

    private Cart buildCart(Long id, Long customerId) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setCustomerId(customerId);
        return cart;
    }

    private CartItem buildCartItem(Long id, Long cartId, Long productId, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setCartId(cartId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }
}
