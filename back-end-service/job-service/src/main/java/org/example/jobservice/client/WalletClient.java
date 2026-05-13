package org.example.jobservice.client;

import org.example.jobservice.dto.EscrowRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8082}", contextId = "walletClient")
public interface WalletClient {

    @PostMapping("/internal/wallet/escrow-hold")
    void freelancerEscrowHold(@RequestBody EscrowRequest request);

    @PostMapping("/internal/wallet/escrow-release")
    void freelancerEscrowRelease(@RequestBody EscrowRequest request);

    @PostMapping("/internal/wallet/client-hold")
    void clientEscrowHold(@RequestBody EscrowRequest request);

    @PostMapping("/internal/wallet/client-release")
    void clientEscrowRelease(@RequestBody EscrowRequest request);
}
