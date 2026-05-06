package org.example.userservice.dto;

import lombok.*;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class WalletSummaryDTO {
    private WalletDTO wallet;
    private List<TransactionDTO> recentTransactions;
}
