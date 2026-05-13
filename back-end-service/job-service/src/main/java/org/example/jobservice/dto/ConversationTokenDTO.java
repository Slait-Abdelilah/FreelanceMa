package org.example.jobservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationTokenDTO {
    private String firebaseToken;
    private String conversationId;
    private Long otherUserId;
    private String otherUserRole;
    private String offerTitle;
}
