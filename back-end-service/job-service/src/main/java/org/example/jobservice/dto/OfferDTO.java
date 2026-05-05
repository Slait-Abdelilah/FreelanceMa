package org.example.jobservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private Long id;
    private Long clientId;
    private String title;
    private String description;
    private String category;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String budgetType;
    private LocalDate deadline;
    private String requiredSkills;
    private String status;
    private Integer applicationsCount;
    private LocalDateTime createdAt;
}
