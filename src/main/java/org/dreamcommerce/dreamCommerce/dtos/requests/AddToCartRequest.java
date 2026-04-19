package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private Long customerId;
    private Long productId;
    private Integer quantity;
}
