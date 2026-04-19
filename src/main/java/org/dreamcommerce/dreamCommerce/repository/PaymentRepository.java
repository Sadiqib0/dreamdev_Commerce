package org.dreamcommerce.dreamCommerce.repository;

import org.dreamcommerce.dreamCommerce.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
