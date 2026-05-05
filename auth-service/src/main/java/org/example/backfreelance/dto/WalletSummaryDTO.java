package org.example.backfreelance.dto;


import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletSummaryDTO {
    private WalletDTO wallet;
    private List<TransactionDTO> recentTransactions;
}
