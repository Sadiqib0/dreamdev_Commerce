package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.AddProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.AddProductResponse;
import org.dreamcommerce.dreamCommerce.dtos.responses.ProductResponse;
import org.dreamcommerce.dreamCommerce.models.Product;
import org.dreamcommerce.dreamCommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public AddProductResponse addProduct(AddProductRequest productRequest) {
        ModelMapper mapper = new ModelMapper();
        Product product = mapper.map(productRequest, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, AddProductResponse.class);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        ModelMapper mapper = new ModelMapper();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.map(product, ProductResponse.class);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        ModelMapper mapper = new ModelMapper();
        return productRepository.findAll().stream()
                .map(product -> mapper.map(product, ProductResponse.class))
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        ModelMapper mapper = new ModelMapper();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        mapper.map(request, product);
        return mapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
