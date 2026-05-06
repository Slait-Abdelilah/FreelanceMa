package org.example.userservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
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
    private Double averageRating;
    private Integer totalReviews;
    private Integer completedMissions;
}
