package org.example.backfreelance.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backfreelance.dto.*;
import org.example.backfreelance.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // GET /api/wallet → mon wallet + 10 dernières transactions
    @GetMapping
    public ResponseEntity<WalletSummaryDTO> getMyWallet(Authentication auth) {
        return ResponseEntity.ok(walletService.getMyWallet(auth.getName()));
    }

    // GET /api/wallet/transactions → toutes les transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(Authentication auth) {
        return ResponseEntity.ok(walletService.getAllTransactions(auth.getName()));
    }

    // POST /api/wallet/withdraw → demande de retrait
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> requestWithdrawal(
            Authentication auth,
            @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(
                walletService.requestWithdrawal(auth.getName(), request)
        );
    }
}
