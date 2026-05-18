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

    // Appelé quand le client accepte une candidature : déduit de son solde et bloque en pending
    @PostMapping("/client-hold")
    public ResponseEntity<TransactionDTO> clientHold(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.clientEscrowHold(request));
    }

    // Appelé quand la mission est validée : libère le pending du client
    @PostMapping("/client-release")
    public ResponseEntity<TransactionDTO> clientRelease(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.clientEscrowRelease(request));
    }

    // Appelé quand la mission est validée : crédite directement le solde du freelancer
    @PostMapping("/direct-credit")
    public ResponseEntity<TransactionDTO> directCredit(@RequestBody EscrowRequest request) {
        return ResponseEntity.ok(walletService.directCredit(request));
    }
}
