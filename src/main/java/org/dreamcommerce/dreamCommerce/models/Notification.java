package org.dreamcommerce.dreamCommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.NotificationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    private LocalDateTime createdAt = LocalDateTime.now();
}
