package org.example.backfreelance.controller;

import jakarta.validation.Valid;
import org.example.backfreelance.dto.*;
import org.example.backfreelance.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Inscription réussie, vérifiez votre email !");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Compte activé avec succès !");
    }

    @PostMapping("/verify-account")
    public ResponseEntity<AuthResponse> verifyAccount(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        return ResponseEntity.ok(authService.verifyAccount(email, code));
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestBody Map<String, String> body) {
        authService.resendVerificationCode(body.get("email"));
        return ResponseEntity.ok("Code renvoyé par email");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Si cet email existe, un code de réinitialisation a été envoyé");
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verifyResetCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        authService.verifyResetCode(email, code);
        return ResponseEntity.ok("Code valide");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Mot de passe modifié avec succès !");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Déconnecté avec succès");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(Authentication auth,
                                                  @Valid @RequestBody UpdatePasswordRequest request) {
        authService.changePassword(auth.getName(), request);
        return ResponseEntity.ok("Mot de passe modifié avec succès");
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteAccount(Authentication auth) {
        authService.deleteAccount(auth.getName());
        return ResponseEntity.ok("Compte supprimé avec succès");
    }
}