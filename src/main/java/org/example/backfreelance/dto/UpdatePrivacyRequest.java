package org.example.backfreelance.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePrivacyRequest {
    private Boolean publicProfile;
    private Boolean showEarnings;
    private Boolean showOnlineStatus;
}
