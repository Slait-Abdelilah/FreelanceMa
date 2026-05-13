package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.ApplicationDTO;
import org.example.jobservice.dto.CreateApplicationRequest;
import org.example.jobservice.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // FREELANCER seulement — contrôlé dans SecurityConfig
    @PostMapping
    public ResponseEntity<ApplicationDTO> apply(
            Authentication auth,
            @Valid @RequestBody CreateApplicationRequest request) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.status(201).body(applicationService.apply(freelancerId, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationDTO>> getMyApplications(Authentication auth) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.getMyApplications(freelancerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplication(Authentication auth, @PathVariable Long id) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.getApplicationById(freelancerId, id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApplicationDTO> complete(Authentication auth, @PathVariable Long id) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.completeApplication(freelancerId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> withdraw(Authentication auth, @PathVariable Long id) {
        Long freelancerId = (Long) auth.getDetails();
        applicationService.withdrawApplication(freelancerId, id);
        return ResponseEntity.ok("Candidature retirée");
    }

    // ── CLIENT ─────────────────────────────────────────────────────────────

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApplicationDTO> accept(Authentication auth, @PathVariable Long id) {
        Long clientId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.acceptApplication(clientId, id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApplicationDTO> reject(Authentication auth, @PathVariable Long id) {
        Long clientId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.rejectApplication(clientId, id));
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<ApplicationDTO> validate(Authentication auth, @PathVariable Long id) {
        Long clientId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.validateMission(clientId, id));
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<List<ApplicationDTO>> getForOffer(Authentication auth, @PathVariable Long offerId) {
        Long clientId = (Long) auth.getDetails();
        return ResponseEntity.ok(applicationService.getApplicationsForOffer(clientId, offerId));
    }
}