package org.example.backfreelance.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {
    private Long id;
    private BigDecimal balance;           // solde disponible
    private BigDecimal pendingBalance;    // en attente (escrow)
    private BigDecimal totalEarned;       // total gagné
    private LocalDateTime createdAt;
}
