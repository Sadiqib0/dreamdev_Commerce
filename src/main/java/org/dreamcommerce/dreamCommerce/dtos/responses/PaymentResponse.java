package org.dreamcommerce.dreamCommerce.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.PaymentMethod;
import org.dreamcommerce.dreamCommerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private LocalDateTime createdAt;
}
