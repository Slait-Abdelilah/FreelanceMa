package org.example.backfreelance.controller;


import lombok.RequiredArgsConstructor;
import org.example.backfreelance.dto.ProfileDTO;
import org.example.backfreelance.dto.UpdateProfileRequest;
import org.example.backfreelance.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileDTO> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(profileService.getProfile(auth.getName()));
    }

    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(
            Authentication auth,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(auth.getName(), request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getPublicProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getPublicProfile(userId));
    }
}
