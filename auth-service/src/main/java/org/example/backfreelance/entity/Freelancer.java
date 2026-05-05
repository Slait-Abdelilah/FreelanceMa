package org.example.backfreelance.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.backfreelance.enums.ExperienceLevel;

import java.math.BigDecimal;

@Entity
@Table(name = "freelancers")
@DiscriminatorValue("FREELANCER")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Freelancer extends User {
    private String title;
    private String bio;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private BigDecimal hourlyRate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ExperienceLevel experienceLevel = ExperienceLevel.JUNIOR;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private Integer completedMissions = 0;

    @Builder.Default
    private Boolean isAvailable = true;

    @Builder.Default
    private Boolean publicProfile = true;

    @Builder.Default
    private Boolean showEarnings = false;

    private String portfolioUrl;
    private String githubUrl;
    private String linkedinUrl;
}
