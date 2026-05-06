package org.example.userservice.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdatePrivacyRequest {
    private Boolean showOnlineStatus;
    private Boolean publicProfile;
    private Boolean showEarnings;
}
