package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    // userId = même valeur que l'id dans auth-service (vient du JWT)
    @Id
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;
    private String role;          // "CLIENT" ou "FREELANCER"

    // ── Paramètres communs ────────────────────────────────────────────────
    private String  phone;
    @Builder.Default private String  language        = "fr";
    @Builder.Default private String  currency        = "MAD";
    @Builder.Default private Boolean showOnlineStatus = true;
    @Builder.Default private Boolean publicProfile    = true;

    // ── Profil Freelancer (null pour les clients) ─────────────────────────
    private String     title;
    private String     bio;
    private String     location;
    @Column(columnDefinition = "TEXT")
    private String     skills;
    private BigDecimal hourlyRate;
    private String     experienceLevel;
    @Builder.Default private Boolean isAvailable     = true;
    @Builder.Default private Boolean showEarnings    = false;
    private String     portfolioUrl;
    private String     githubUrl;
    private String     linkedinUrl;
    @Builder.Default private Double  averageRating   = 0.0;
    @Builder.Default private Integer totalReviews    = 0;
    @Builder.Default private Integer completedMissions = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
