package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.ProfileDTO;
import org.example.userservice.dto.UpdateProfileRequest;
import org.example.userservice.service.ProfileService;
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
        return ResponseEntity.ok(profileService.getProfile(auth.getName(), userId(auth), role(auth)));
    }

    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(Authentication auth,
                                                     @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(auth.getName(), userId(auth), role(auth), request));
    }

    @GetMapping("/{userId}/public")
    public ResponseEntity<ProfileDTO> getPublicProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getPublicProfile(userId));
    }

    private Long userId(Authentication auth) {
        return (Long) auth.getDetails();
    }

    private String role(Authentication auth) {
        return auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("FREELANCER");
    }
}
