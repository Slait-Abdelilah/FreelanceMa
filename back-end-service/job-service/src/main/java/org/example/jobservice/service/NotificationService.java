package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.NotificationDTO;
import org.example.jobservice.entity.Notification;
import org.example.jobservice.enums.NotificationType;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void create(Long userId, NotificationType type, String title, String message,
                       Long offerId, Long applicationId) {
        notificationRepository.save(
            Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .offerId(offerId)
                .applicationId(applicationId)
                .build()
        );
    }

    public List<NotificationDTO> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getUnreadCount(Long userId) {
        return Map.of("count", notificationRepository.countByUserIdAndIsReadFalse(userId));
    }

    @Transactional
    public NotificationDTO markAsRead(Long userId, Long notifId) {
        Notification n = notificationRepository.findById(notifId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable"));
        if (!n.getUserId().equals(userId)) throw new ForbiddenException("Accès refusé");
        n.setRead(true);
        return toDTO(notificationRepository.save(n));
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllReadByUserId(userId);
    }

    @Transactional
    public void delete(Long userId, Long notifId) {
        Notification n = notificationRepository.findById(notifId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable"));
        if (!n.getUserId().equals(userId)) throw new ForbiddenException("Accès refusé");
        notificationRepository.delete(n);
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .type(n.getType().name())
                .title(n.getTitle())
                .message(n.getMessage())
                .offerId(n.getOfferId())
                .applicationId(n.getApplicationId())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
