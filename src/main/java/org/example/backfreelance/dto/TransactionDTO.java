package org.example.backfreelance.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String type;       // DEPOSIT, WITHDRAWAL, ESCROW_HOLD...
    private String status;     // PENDING, COMPLETED...
    private String description;
    private Long missionId;
    private LocalDateTime createdAt;
}