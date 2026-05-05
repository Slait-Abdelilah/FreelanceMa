package org.example.backfreelance.controller;

import lombok.AllArgsConstructor;
import org.example.backfreelance.dto.CreatePortfolioRequest;
import org.example.backfreelance.dto.PortfolioDTO;
import org.example.backfreelance.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@AllArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    // GET /api/portfolio → mes projets
    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> getMyPortfolio(Authentication auth) {
        return ResponseEntity.ok(
                portfolioService.getMyPortfolio(auth.getName())
        );
    }

    // POST /api/portfolio → ajouter un projet
    @PostMapping
    public ResponseEntity<PortfolioDTO> addProject(
            Authentication auth,
            @RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(
                portfolioService.addProject(auth.getName(), request)
        );
    }

    // PUT /api/portfolio/{id} → modifier un projet
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO> updateProject(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(
                portfolioService.updateProject(auth.getName(), id, request)
        );
    }

    // DELETE /api/portfolio/{id} → supprimer un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            Authentication auth,
            @PathVariable Long id) {
        portfolioService.deleteProject(auth.getName(), id);
        return ResponseEntity.ok("Projet supprimé");
    }

    // GET /api/portfolio/public/{freelancerId} → portfolio public
    @GetMapping("/public/{freelancerId}")
    public ResponseEntity<List<PortfolioDTO>> getPublicPortfolio(
            @PathVariable Long freelancerId) {
        return ResponseEntity.ok(
                portfolioService.getPublicPortfolio(freelancerId)
        );
    }
}
