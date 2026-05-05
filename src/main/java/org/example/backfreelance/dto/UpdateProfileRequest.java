package org.example.backfreelance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String title;
    private String bio;
    private String location;
    private String skills;
    private BigDecimal hourlyRate;
    private String experienceLevel;
    private Boolean isAvailable;
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
}
