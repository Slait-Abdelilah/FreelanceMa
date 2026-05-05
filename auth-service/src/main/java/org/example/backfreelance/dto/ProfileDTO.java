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
public class ProfileDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    // infos modifiables
    private String title;           // "Développeur Vue.js Senior"
    private String bio;             // description personnelle
    private String location;        // "Fès, Maroc"
    private String skills;          // "Vue.js, Spring Boot, MariaDB"
    private BigDecimal hourlyRate;  // 150 DH/h
    private String experienceLevel; // JUNIOR, MID, SENIOR
    private Boolean isAvailable;    // disponible ?

    // liens
    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;

    // stats (lecture seule)
    private Double averageRating;
    private Integer totalReviews;
    private Integer completedMissions;
}
