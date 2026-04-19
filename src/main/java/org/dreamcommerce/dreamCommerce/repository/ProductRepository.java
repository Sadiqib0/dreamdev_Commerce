package org.dreamcommerce.dreamCommerce.repository;

import org.dreamcommerce.dreamCommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
