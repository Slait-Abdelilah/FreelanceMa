package org.example.jobservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String type;
    private String title;
    private String message;
    private Long offerId;
    private Long applicationId;
    private boolean isRead;
    private LocalDateTime createdAt;
}
