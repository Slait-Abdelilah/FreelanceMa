package org.example.backfreelance.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "portfolios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // référence vers le freelancer
    @Column(name = "freelancer_id", nullable = false)
    private Long freelancerId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String technologies;  // "Vue.js, Spring Boot, MariaDB"
    private String imageUrl;      // URL de l'image du projet
    private String projectUrl;    // lien vers le projet en ligne
    private String githubUrl;     // lien GitHub

    @Builder.Default
    private Integer displayOrder = 0;  // ordre d'affichage

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
