package org.example.jobservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private Long id;
    private Long offerId;
    private Long freelancerId;
    private String coverLetter;
    private BigDecimal proposedBudget;
    private Integer proposedDays;
    private String status;
    private LocalDateTime createdAt;
    private String offerTitle;

    private String offerCategory;
    private BigDecimal offerBudgetMin;
    private BigDecimal offerBudgetMax;
}
