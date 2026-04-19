package org.dreamcommerce.dreamCommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();
}
