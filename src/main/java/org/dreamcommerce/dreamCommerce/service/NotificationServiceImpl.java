package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.CreateNotificationRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.NotificationResponse;
import org.dreamcommerce.dreamCommerce.enums.NotificationStatus;
import org.dreamcommerce.dreamCommerce.models.Notification;
import org.dreamcommerce.dreamCommerce.repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        ModelMapper mapper = new ModelMapper();
        Notification notification = mapper.map(request, Notification.class);
        return mapper.map(notificationRepository.save(notification), NotificationResponse.class);
    }

    @Override
    public NotificationResponse getNotification(Long id) {
        ModelMapper mapper = new ModelMapper();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return mapper.map(notification, NotificationResponse.class);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        ModelMapper mapper = new ModelMapper();
        return notificationRepository.findByUserId(userId).stream()
                .map(n -> mapper.map(n, NotificationResponse.class))
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        ModelMapper mapper = new ModelMapper();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(NotificationStatus.READ);
        return mapper.map(notificationRepository.save(notification), NotificationResponse.class);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteStaleNotifications() {
        notificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(30));
    }
}
