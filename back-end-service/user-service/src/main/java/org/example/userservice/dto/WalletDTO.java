package org.example.userservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class WalletDTO {
    private Long id;
    private BigDecimal balance;
    private BigDecimal pendingBalance;
    private BigDecimal totalEarned;
    private LocalDateTime createdAt;
}
