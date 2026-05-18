package org.example.jobservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.dto.ChatMessageDTO;
import org.example.jobservice.dto.ConversationTokenDTO;
import org.example.jobservice.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/token")
    public ResponseEntity<ConversationTokenDTO> getConversationToken(
            @RequestParam Long applicationId,
            Authentication auth) {
        Long   userId = (Long) auth.getDetails();
        String role   = auth.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return ResponseEntity.ok(messageService.getConversationToken(applicationId, userId, role));
    }

    @GetMapping("/firebase-health")
    public ResponseEntity<Map<String, String>> firebaseHealth() {
        boolean ok = messageService.isFirebaseAvailable();
        if (ok) return ResponseEntity.ok(Map.of("status", "OK"));
        return ResponseEntity.status(503).body(Map.of(
            "status", "ERROR",
            "message", "Firebase indisponible — régénérer firebase-service-account.json depuis Firebase Console"
        ));
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(
            @PathVariable String conversationId,
            Authentication auth) {
        Long   userId = (Long) auth.getDetails();
        String role   = auth.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return ResponseEntity.ok(messageService.getHistory(conversationId, userId, role));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication auth) {
        Long   userId = (Long) auth.getDetails();
        String role   = auth.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return ResponseEntity.ok(Map.of("count", messageService.getUnreadCount(userId, role)));
    }
}
