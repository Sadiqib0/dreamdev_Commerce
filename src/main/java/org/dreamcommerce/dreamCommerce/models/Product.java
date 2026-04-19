package org.dreamcommerce.dreamCommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    private String category;

    private Integer stock;

    private Long vendorId;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();
}
