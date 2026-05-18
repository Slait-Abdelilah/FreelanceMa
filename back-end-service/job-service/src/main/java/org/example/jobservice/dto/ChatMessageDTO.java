package org.example.jobservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatMessageDTO {
    private Long   id;
    private String conversationId;
    private String senderId;
    private String senderRole;
    private String content;
    private Instant createdAt;
}
