package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.PaymentMethod;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod method;
}
