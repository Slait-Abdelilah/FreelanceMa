package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.*;
import org.example.userservice.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletSummaryDTO> getMyWallet(Authentication auth) {
        return ResponseEntity.ok(walletService.getMyWallet(auth.getName()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(Authentication auth) {
        return ResponseEntity.ok(walletService.getAllTransactions(auth.getName()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> requestWithdrawal(Authentication auth,
                                                             @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(walletService.requestWithdrawal(auth.getName(), request));
    }
}
