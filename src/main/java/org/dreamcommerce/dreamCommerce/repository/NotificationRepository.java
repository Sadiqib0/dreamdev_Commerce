package org.dreamcommerce.dreamCommerce.repository;

import org.dreamcommerce.dreamCommerce.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
