package org.dreamcommerce.dreamCommerce.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private Integer stock;
    private Long vendorId;
    private ProductStatus status;
    private LocalDateTime createdAt;
}
