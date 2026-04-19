package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateOrderRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.OrderResponse;
import org.dreamcommerce.dreamCommerce.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrder(Long id);
    List<OrderResponse> getOrdersByCustomer(Long customerId);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
