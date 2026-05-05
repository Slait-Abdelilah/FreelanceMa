package org.example.backfreelance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequest {

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "100", message = "Montant minimum de retrait : 100 DH")
    private BigDecimal amount;

    @NotBlank(message = "L'IBAN est obligatoire")
    private String iban;
}