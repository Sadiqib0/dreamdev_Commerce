package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private Integer stock;
    private Long vendorId;
}
