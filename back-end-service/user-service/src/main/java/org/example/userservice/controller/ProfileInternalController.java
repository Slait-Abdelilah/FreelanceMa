package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/profile")
@RequiredArgsConstructor
public class ProfileInternalController {

    private final ProfileService profileService;

    @PutMapping("/{userId}/rating")
    public ResponseEntity<Void> updateRating(@PathVariable Long userId,
                                             @RequestParam double averageRating,
                                             @RequestParam int totalReviews) {
        profileService.updateRating(userId, averageRating, totalReviews);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/completed-missions")
    public ResponseEntity<Void> incrementCompletedMissions(@PathVariable Long userId) {
        profileService.incrementCompletedMissions(userId);
        return ResponseEntity.ok().build();
    }
}
