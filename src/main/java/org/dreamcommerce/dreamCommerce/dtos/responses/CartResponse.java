package org.dreamcommerce.dreamCommerce.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CartResponse {
    private Long id;
    private Long customerId;
    private LocalDateTime createdAt;
}
