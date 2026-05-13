package org.example.jobservice.client;

import org.example.jobservice.dto.EscrowRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8082}", contextId = "walletClient")
public interface WalletClient {

    // Called when client accepts an application: deducts from client balance
    @PostMapping("/internal/wallet/client-hold")
    void clientEscrowHold(@RequestBody EscrowRequest request);

    // Called when client validates: releases client pendingBalance
    @PostMapping("/internal/wallet/client-release")
    void clientEscrowRelease(@RequestBody EscrowRequest request);

    // Called when client validates: credits freelancer balance directly
    @PostMapping("/internal/wallet/direct-credit")
    void freelancerDirectCredit(@RequestBody EscrowRequest request);
}
