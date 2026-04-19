package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private Long customerId;
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
