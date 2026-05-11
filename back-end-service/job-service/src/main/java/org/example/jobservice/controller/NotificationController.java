package org.example.jobservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.NotificationDTO;
import org.example.jobservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAll(Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markRead(Authentication auth, @PathVariable Long id) {
        Long userId = (Long) auth.getDetails();
        return ResponseEntity.ok(notificationService.markAsRead(userId, id));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllRead(Authentication auth) {
        Long userId = (Long) auth.getDetails();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        Long userId = (Long) auth.getDetails();
        notificationService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
