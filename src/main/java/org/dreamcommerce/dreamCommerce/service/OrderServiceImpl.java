package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.CreateOrderRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.OrderResponse;
import org.dreamcommerce.dreamCommerce.enums.OrderStatus;
import org.dreamcommerce.dreamCommerce.models.Order;
import org.dreamcommerce.dreamCommerce.models.OrderItem;
import org.dreamcommerce.dreamCommerce.repository.OrderItemRepository;
import org.dreamcommerce.dreamCommerce.repository.OrderRepository;
import org.dreamcommerce.dreamCommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        ModelMapper mapper = new ModelMapper();

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());

        BigDecimal total = request.getItems().stream()
                .map(item -> {
                    var product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        request.getItems().forEach(item -> {
            var product = productRepository.findById(item.getProductId()).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
        });

        return mapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        ModelMapper mapper = new ModelMapper();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        ModelMapper mapper = new ModelMapper();
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order -> mapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        ModelMapper mapper = new ModelMapper();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return mapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
