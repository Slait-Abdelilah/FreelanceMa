package org.example.jobservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequest {

    @NotNull(message = "L'identifiant de l'offre est obligatoire")
    private Long offerId;

    @NotBlank(message = "La lettre de motivation est obligatoire")
    private String coverLetter;

    @NotNull(message = "Le budget proposé est obligatoire")
    @Positive(message = "Le budget proposé doit être positif")
    private BigDecimal proposedBudget;

    @NotNull(message = "Le délai proposé est obligatoire")
    @Positive(message = "Le délai proposé doit être positif")
    private Integer proposedDays;
}
