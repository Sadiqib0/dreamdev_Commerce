package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.AddProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateProductRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.AddProductResponse;
import org.dreamcommerce.dreamCommerce.dtos.responses.ProductResponse;
import org.dreamcommerce.dreamCommerce.enums.ProductStatus;
import org.dreamcommerce.dreamCommerce.models.Product;
import org.dreamcommerce.dreamCommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCanAddProduct() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Test Product");
        request.setPrice(BigDecimal.valueOf(99.99));

        Product savedProduct = buildProduct(1L, "Test Product", BigDecimal.valueOf(99.99));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        AddProductResponse response = productService.addProduct(request);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void testCanGetProduct() {
        Product product = buildProduct(1L, "Laptop", BigDecimal.valueOf(1500.00));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProduct(1L);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop");
    }

    @Test
    void testGetProductThrowsWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found");
    }

    @Test
    void testCanGetAllProducts() {
        Product p1 = buildProduct(1L, "Phone", BigDecimal.valueOf(500.00));
        Product p2 = buildProduct(2L, "Tablet", BigDecimal.valueOf(800.00));
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductResponse> responses = productService.getAllProducts();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void testCanUpdateProduct() {
        Product existing = buildProduct(1L, "Old Name", BigDecimal.valueOf(100.00));
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("New Name");
        request.setPrice(BigDecimal.valueOf(200.00));

        ProductResponse response = productService.updateProduct(1L, request);

        assertNotNull(response);
        verify(productRepository).save(existing);
    }

    @Test
    void testCanDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    private Product buildProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setStatus(ProductStatus.ACTIVE);
        return product;
    }
}
