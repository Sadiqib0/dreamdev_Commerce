package org.dreamcommerce.dreamCommerce.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.NotificationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
