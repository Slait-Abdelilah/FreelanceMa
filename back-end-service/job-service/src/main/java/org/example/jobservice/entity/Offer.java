package org.example.jobservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jobservice.enums.BudgetType;
import org.example.jobservice.enums.OfferCategory;
import org.example.jobservice.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID du client (référence vers auth-service/user-service)
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferCategory category;

    @Column(precision = 10, scale = 2)
    private BigDecimal budgetMin;

    @Column(precision = 10, scale = 2)
    private BigDecimal budgetMax;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BudgetType budgetType = BudgetType.FIXED;

    private LocalDate deadline;

    @Column(name = "required_skills")
    private String requiredSkills; // "Vue.js, Spring Boot"

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OfferStatus status = OfferStatus.OPEN;

    @Builder.Default
    private Integer applicationsCount = 0;

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