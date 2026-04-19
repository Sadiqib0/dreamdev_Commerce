package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.AddProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.AddProductResponse;
import org.dreamcommerce.dreamCommerce.dtos.responses.ProductResponse;

import java.util.List;

public interface ProductService {
    AddProductResponse addProduct(AddProductRequest productRequest);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}
