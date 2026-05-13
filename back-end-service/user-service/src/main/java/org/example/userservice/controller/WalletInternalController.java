package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.EscrowRequest;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/wallet")
@RequiredArgsConstructor
public class WalletInternalController {

    private final WalletService walletService;

    @PostMapping("/escrow-hold")
    public ResponseEntity<TransactionDTO> escrowHold(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.escrowHold(request));
    }

    @PostMapping("/escrow-release")
    public ResponseEntity<TransactionDTO> escrowRelease(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.escrowRelease(request));
    }

    @PostMapping("/client-hold")
    public ResponseEntity<TransactionDTO> clientHold(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.clientEscrowHold(request));
    }

    @PostMapping("/client-release")
    public ResponseEntity<TransactionDTO> clientRelease(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.clientEscrowRelease(request));
    }

    @PostMapping("/direct-credit")
    public ResponseEntity<TransactionDTO> directCredit(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.directCredit(request));
    }
}
