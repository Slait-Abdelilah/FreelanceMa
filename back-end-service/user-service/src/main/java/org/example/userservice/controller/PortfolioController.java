package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.CreatePortfolioRequest;
import org.example.userservice.dto.PortfolioDTO;
import org.example.userservice.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> getMyPortfolio(Authentication auth) {
        return ResponseEntity.ok(portfolioService.getMyPortfolio(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<PortfolioDTO> addProject(Authentication auth,
                                                    @RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.addProject(auth.getName(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO> updateProject(Authentication auth,
                                                       @PathVariable Long id,
                                                       @RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.updateProject(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(Authentication auth, @PathVariable Long id) {
        portfolioService.deleteProject(auth.getName(), id);
        return ResponseEntity.ok("Projet supprimé");
    }

    @GetMapping("/public/{freelancerId}")
    public ResponseEntity<List<PortfolioDTO>> getPublicPortfolio(@PathVariable Long freelancerId) {
        return ResponseEntity.ok(portfolioService.getPublicPortfolio(freelancerId));
    }
}
