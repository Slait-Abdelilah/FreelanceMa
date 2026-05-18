package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.DepositRequest;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.dto.WalletSummaryDTO;
import org.example.userservice.dto.WithdrawalRequest;
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
        return ResponseEntity.ok(walletService.getMyWallet(getUserId(auth)));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(Authentication auth) {
        return ResponseEntity.ok(walletService.getAllTransactions(getUserId(auth)));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(Authentication auth,
                                                   @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(getUserId(auth), request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> requestWithdrawal(Authentication auth,
                                                             @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(walletService.requestWithdrawal(getUserId(auth), request));
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getDetails();
    }
}
