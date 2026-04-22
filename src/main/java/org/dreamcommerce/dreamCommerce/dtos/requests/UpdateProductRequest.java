package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.ProductStatus;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private List<MultipartFile> images;
    private String category;
    private Integer stock;
    private ProductStatus status;
}
