package org.example.jobservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    private String category;

    @Positive(message = "Le budget minimum doit être positif")
    private BigDecimal budgetMin;

    @Positive(message = "Le budget maximum doit être positif")
    private BigDecimal budgetMax;

    private String budgetType;

    @Future(message = "La date limite doit être dans le futur")
    private LocalDate deadline;

    private String requiredSkills;
}
