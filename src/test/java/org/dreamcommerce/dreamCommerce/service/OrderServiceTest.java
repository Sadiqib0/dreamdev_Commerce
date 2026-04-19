package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateOrderRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.OrderResponse;
import org.dreamcommerce.dreamCommerce.enums.OrderStatus;
import org.dreamcommerce.dreamCommerce.models.Order;
import org.dreamcommerce.dreamCommerce.models.OrderItem;
import org.dreamcommerce.dreamCommerce.models.Product;
import org.dreamcommerce.dreamCommerce.repository.OrderItemRepository;
import org.dreamcommerce.dreamCommerce.repository.OrderRepository;
import org.dreamcommerce.dreamCommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCanCreateOrder() {
        Product product = buildProduct(10L, BigDecimal.valueOf(200.00));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        Order savedOrder = buildOrder(1L, 5L, BigDecimal.valueOf(400.00));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(5L);
        CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
        item.setProductId(10L);
        item.setQuantity(2);
        request.setItems(List.of(item));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void testCreateOrderThrowsWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);
        CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
        item.setProductId(99L);
        item.setQuantity(1);
        request.setItems(List.of(item));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void testCanGetOrder() {
        Order order = buildOrder(1L, 5L, BigDecimal.valueOf(200.00));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrder(1L);

        assertNotNull(response);
        assertThat(response.getCustomerId()).isEqualTo(5L);
    }

    @Test
    void testGetOrderThrowsWhenNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrder(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");
    }

    @Test
    void testCanGetOrdersByCustomer() {
        Order o1 = buildOrder(1L, 5L, BigDecimal.valueOf(100.00));
        Order o2 = buildOrder(2L, 5L, BigDecimal.valueOf(200.00));
        when(orderRepository.findByCustomerId(5L)).thenReturn(List.of(o1, o2));

        List<OrderResponse> responses = orderService.getOrdersByCustomer(5L);

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void testCanUpdateOrderStatus() {
        Order order = buildOrder(1L, 5L, BigDecimal.valueOf(100.00));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertNotNull(response);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    void testCanDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    private Order buildOrder(Long id, Long customerId, BigDecimal total) {
        Order order = new Order();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    private Product buildProduct(Long id, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(price);
        return product;
    }
}
