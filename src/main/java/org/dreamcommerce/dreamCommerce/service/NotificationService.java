package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateNotificationRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse createNotification(CreateNotificationRequest request);
    NotificationResponse getNotification(Long id);
    List<NotificationResponse> getNotificationsByUser(Long userId);
    NotificationResponse markAsRead(Long id);
    void deleteNotification(Long id);
    void deleteStaleNotifications();
}
