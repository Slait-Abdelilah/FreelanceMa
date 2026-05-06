package org.example.userservice.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserSettingsDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String language;
    private String currency;
    private Boolean showOnlineStatus;
    private Boolean publicProfile;
    private Boolean showEarnings;
    private String role;
}
