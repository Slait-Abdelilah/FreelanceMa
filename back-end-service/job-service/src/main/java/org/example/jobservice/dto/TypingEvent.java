package org.example.jobservice.dto;

import lombok.Data;

@Data
public class TypingEvent {
    private String  userId;
    private boolean typing;
}
