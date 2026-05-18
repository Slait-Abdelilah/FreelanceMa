package org.example.userservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UpdateProfileRequest {
    // Common fields (both roles)
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    // Client-specific fields
    private String companyName;
    private String website;
    private String sector;
    // Freelancer-specific fields
    private String title;
    private String skills;
    private BigDecimal hourlyRate;
    private String experienceLevel;
    private Boolean isAvailable;
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
}
