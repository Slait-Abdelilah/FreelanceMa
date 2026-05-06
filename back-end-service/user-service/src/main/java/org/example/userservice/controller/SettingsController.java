package org.example.userservice.controller;

import jakarta.validation.Valid;
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
        return ResponseEntity.ok(settingsService.getSettings(auth.getName()));
    }

    @PutMapping("/account")
    public ResponseEntity<UserSettingsDTO> updateAccount(Authentication auth,
                                                          @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(settingsService.updateAccount(auth.getName(), request));
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(Authentication auth,
                                                  @Valid @RequestBody UpdatePasswordRequest request) {
        settingsService.updatePassword(auth.getName(), request);
        return ResponseEntity.ok("Mot de passe modifié avec succès");
    }

    @PutMapping("/privacy")
    public ResponseEntity<UserSettingsDTO> updatePrivacy(Authentication auth,
                                                          @RequestBody UpdatePrivacyRequest request) {
        return ResponseEntity.ok(settingsService.updatePrivacy(auth.getName(), request));
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteAccount(Authentication auth) {
        settingsService.deleteAccount(auth.getName());
        return ResponseEntity.ok("Compte supprimé");
    }
}
