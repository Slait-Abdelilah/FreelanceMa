package org.example.jobservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscrowRequest {
    private Long userId;
    private BigDecimal amount;
    private Long missionId;
    private String description;
}
