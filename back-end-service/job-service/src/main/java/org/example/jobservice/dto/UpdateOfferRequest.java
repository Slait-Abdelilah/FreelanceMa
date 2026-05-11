package org.example.jobservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateOfferRequest {
    private String title;
    private String description;
    private String category;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String budgetType;
    private LocalDate deadline;
    private String requiredSkills;
}
