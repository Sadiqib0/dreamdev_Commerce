package org.dreamcommerce.dreamCommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.PaymentMethod;
import org.dreamcommerce.dreamCommerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private LocalDateTime createdAt = LocalDateTime.now();
}
