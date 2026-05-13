package org.example.jobservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.ConversationTokenDTO;
import org.example.jobservice.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/token")
    public ResponseEntity<ConversationTokenDTO> getToken(
            Authentication auth,
            @RequestParam Long applicationId) {
        Long userId   = (Long) auth.getDetails();
        String role   = auth.getAuthorities().stream()
                .findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return ResponseEntity.ok(messageService.getConversationToken(userId, role, applicationId));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ConversationTokenDTO> refreshToken(Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return ResponseEntity.ok(messageService.refreshToken(userId));
    }
}
