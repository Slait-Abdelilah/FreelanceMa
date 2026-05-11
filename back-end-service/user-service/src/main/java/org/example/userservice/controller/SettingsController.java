package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.*;
import org.example.userservice.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseEntity<UserSettingsDTO> getSettings(Authentication auth) {
        return ResponseEntity.ok(settingsService.getSettings(auth.getName(), userId(auth), role(auth)));
    }

    @PutMapping("/account")
    public ResponseEntity<UserSettingsDTO> updateAccount(Authentication auth,
                                                          @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(settingsService.updateAccount(auth.getName(), userId(auth), role(auth), request));
    }

    @PutMapping("/privacy")
    public ResponseEntity<UserSettingsDTO> updatePrivacy(Authentication auth,
                                                          @RequestBody UpdatePrivacyRequest request) {
        return ResponseEntity.ok(settingsService.updatePrivacy(auth.getName(), userId(auth), role(auth), request));
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
