package org.example.jobservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.FavoriteDTO;
import org.example.jobservice.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getMyFavorites(Authentication auth) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.ok(favoriteService.getMyFavorites(freelancerId));
    }

    @PostMapping("/{offerId}/toggle")
    public ResponseEntity<Map<String, Object>> toggle(Authentication auth, @PathVariable Long offerId) {
        Long freelancerId = (Long) auth.getDetails();
        return ResponseEntity.ok(favoriteService.toggle(freelancerId, offerId));
    }

    @GetMapping("/{offerId}/check")
    public ResponseEntity<Map<String, Object>> check(Authentication auth, @PathVariable Long offerId) {
        Long freelancerId = (Long) auth.getDetails();
        boolean saved = favoriteService.isSaved(freelancerId, offerId);
        return ResponseEntity.ok(Map.of("saved", saved));
    }
}
