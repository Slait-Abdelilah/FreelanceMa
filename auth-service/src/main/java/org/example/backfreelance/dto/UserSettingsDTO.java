package org.example.backfreelance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
