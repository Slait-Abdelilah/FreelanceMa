package org.example.jobservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jobservice.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_id", nullable = false)
    private Long offerId;

    // ID du freelancer (référence vers auth-service)
    @Column(name = "freelancer_id", nullable = false)
    private Long freelancerId;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;     // lettre de motivation

    @Column(precision = 10, scale = 2)
    private BigDecimal proposedBudget;  // devis proposé

    private Integer proposedDays;   // délai proposé en jours

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
