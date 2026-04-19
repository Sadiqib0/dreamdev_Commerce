package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateNotificationRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.NotificationResponse;
import org.dreamcommerce.dreamCommerce.enums.NotificationStatus;
import org.dreamcommerce.dreamCommerce.models.Notification;
import org.dreamcommerce.dreamCommerce.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testCanCreateNotification() {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(1L);
        request.setMessage("Your order has been shipped");

        Notification saved = buildNotification(1L, 1L, "Your order has been shipped", NotificationStatus.UNREAD);
        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        NotificationResponse response = notificationService.createNotification(request);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(NotificationStatus.UNREAD);
    }

    @Test
    void testCanGetNotification() {
        Notification notification = buildNotification(1L, 1L, "Order delivered", NotificationStatus.UNREAD);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationResponse response = notificationService.getNotification(1L);

        assertNotNull(response);
        assertThat(response.getMessage()).isEqualTo("Order delivered");
    }

    @Test
    void testGetNotificationThrowsWhenNotFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.getNotification(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Notification not found");
    }

    @Test
    void testCanGetNotificationsByUser() {
        Notification n1 = buildNotification(1L, 5L, "Order placed", NotificationStatus.UNREAD);
        Notification n2 = buildNotification(2L, 5L, "Order shipped", NotificationStatus.READ);
        when(notificationRepository.findByUserId(5L)).thenReturn(List.of(n1, n2));

        List<NotificationResponse> responses = notificationService.getNotificationsByUser(5L);

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void testCanMarkNotificationAsRead() {
        Notification notification = buildNotification(1L, 1L, "Hello", NotificationStatus.UNREAD);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse response = notificationService.markAsRead(1L);

        assertNotNull(response);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.READ);
        verify(notificationRepository).save(notification);
    }

    @Test
    void testCanDeleteNotification() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    void testDeleteStaleNotificationsCallsRepository() {
        doNothing().when(notificationRepository).deleteByCreatedAtBefore(any(LocalDateTime.class));

        notificationService.deleteStaleNotifications();

        verify(notificationRepository).deleteByCreatedAtBefore(any(LocalDateTime.class));
    }

    private Notification buildNotification(Long id, Long userId, String message, NotificationStatus status) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setStatus(status);
        return notification;
    }
}
