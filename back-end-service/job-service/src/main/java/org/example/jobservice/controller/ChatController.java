package org.example.jobservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.dto.ChatMessageDTO;
import org.example.jobservice.dto.SendMessageRequest;
import org.example.jobservice.dto.TypingEvent;
import org.example.jobservice.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{conversationId}")
    public void handleMessage(
            @DestinationVariable String conversationId,
            @Payload SendMessageRequest request,
            Principal principal) {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        Long   userId = (Long) auth.getDetails();
        String role   = auth.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");

        messageService.checkAccess(conversationId, userId, role);
        ChatMessageDTO dto = messageService.saveMessage(conversationId, userId, role, request.getContent());
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, dto);
        log.debug("[WS] message in {} by userId={}", conversationId, userId);
    }

    @MessageMapping("/chat/{conversationId}/typing")
    public void handleTyping(
            @DestinationVariable String conversationId,
            @Payload TypingEvent event,
            Principal principal) {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        event.setUserId(String.valueOf((Long) auth.getDetails()));
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId + "/typing", event);
    }
}
