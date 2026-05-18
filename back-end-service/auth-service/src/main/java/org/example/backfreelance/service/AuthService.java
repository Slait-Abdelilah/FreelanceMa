package org.example.backfreelance.service;

import org.example.backfreelance.dto.AuthResponse;
import org.example.backfreelance.dto.LoginRequest;
import org.example.backfreelance.dto.RegisterRequest;
import org.example.backfreelance.dto.ResetPasswordRequest;
import org.example.backfreelance.entity.Client;
import org.example.backfreelance.entity.Freelancer;
import org.example.backfreelance.entity.RefreshToken;
import org.example.backfreelance.entity.User;
import org.example.backfreelance.enums.AccountStatus;
import org.example.backfreelance.enums.ClientType;
import org.example.backfreelance.enums.ExperienceLevel;
import org.example.backfreelance.enums.Role;
import org.example.backfreelance.exceptionn.AppException;
import org.example.backfreelance.messaging.UserDeletedEvent;
import org.example.backfreelance.messaging.UserEventPublisher;
import org.example.backfreelance.messaging.UserRegisteredEvent;
import org.example.backfreelance.repository.RefreshTokenRepository;
import org.example.backfreelance.repository.UserRepository;
import org.example.backfreelance.seecuriity.AuditLogger;
import org.example.backfreelance.seecuriity.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${app.jwt.refresh-expiration:2592000000}")
    private long refreshExpirationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final AuditLogger auditLogger;
    private final UserEventPublisher userEventPublisher;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService,
                       AuditLogger auditLogger,
                       UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.auditLogger = auditLogger;
        this.userEventPublisher = userEventPublisher;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Cet email est déjà utilisé", HttpStatus.CONFLICT);
        }
        String verificationCode = generateSecureCode();
        User user;
        if (request.getRole() == Role.CLIENT) {
            user = Client.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountStatus(AccountStatus.PENDING)
                    .verificationToken(verificationCode)
                    .tokenExpirationDate(LocalDateTime.now().plusMinutes(15))
                    .clientType(ClientType.INDIVIDUAL)
                    .build();
        } else if (request.getRole() == Role.FREELANCER) {
            user = Freelancer.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountStatus(AccountStatus.PENDING)
                    .verificationToken(verificationCode)
                    .tokenExpirationDate(LocalDateTime.now().plusMinutes(15))
                    .experienceLevel(ExperienceLevel.JUNIOR)
                    .build();
        } else {
            throw new AppException("Rôle invalide", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        emailService.sendVerificationCodeEmail(request.getEmail(), verificationCode);
        auditLogger.log("REGISTER", request.getEmail());
        return AuthResponse.builder().email(request.getEmail()).build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            auditLogger.logFailure("LOGIN", request.getEmail(), "credentials incorrects");
            throw new AppException("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED);
        }
        if (user.getAccountStatus() == AccountStatus.PENDING) {
            throw new AppException("Compte en attente de vérification", HttpStatus.FORBIDDEN);
        }
        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new AppException("Compte suspendu", HttpStatus.FORBIDDEN);
        }
        String role = (user instanceof Client) ? "CLIENT" : "FREELANCER";
        String accessToken = jwtUtil.generateToken(user.getEmail(), role, user.getId());
        String refreshToken = createRefreshToken(user);
        auditLogger.log("LOGIN", user.getEmail());
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(role)
                .build();
    }

    @Transactional
    public AuthResponse verifyAccount(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
        if (user.getAccountStatus() == AccountStatus.ACTIVE) {
            throw new AppException("Compte déjà activé", HttpStatus.CONFLICT);
        }
        if (!code.equals(user.getVerificationToken())) {
            auditLogger.logFailure("VERIFY_ACCOUNT", email, "code invalide");
            throw new AppException("Code invalide", HttpStatus.BAD_REQUEST);
        }
        if (user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AppException("Code expiré, demandez un nouveau", HttpStatus.BAD_REQUEST);
        }
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setVerificationToken(null);
        user.setTokenExpirationDate(null);
        userRepository.save(user);

        String role = (user instanceof Client) ? "CLIENT" : "FREELANCER";
        String accessToken = jwtUtil.generateToken(user.getEmail(), role, user.getId());
        String refreshToken = createRefreshToken(user);
        auditLogger.log("VERIFY_ACCOUNT", email);

        // Publier APRÈS le commit de transaction — une erreur RabbitMQ ne doit pas
        // annuler l'activation du compte
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), role);
        publishAfterCommit(() -> userEventPublisher.publishUserRegistered(event),
                "user.registered pour userId=" + user.getId());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(role)
                .build();
    }

    @Transactional
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
        if (user.getAccountStatus() == AccountStatus.ACTIVE) {
            throw new AppException("Compte déjà activé", HttpStatus.CONFLICT);
        }
        String code = generateSecureCode();
        user.setVerificationToken(code);
        user.setTokenExpirationDate(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        emailService.sendVerificationCodeEmail(email, code);
        auditLogger.log("RESEND_CODE", email);
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException("Token invalide", HttpStatus.BAD_REQUEST));
        if (user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AppException("Token expiré, demandez un nouveau lien", HttpStatus.BAD_REQUEST);
        }
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setVerificationToken(null);
        user.setTokenExpirationDate(null);
        userRepository.save(user);
        auditLogger.log("VERIFY_EMAIL", user.getEmail());

        String role = (user instanceof Client) ? "CLIENT" : "FREELANCER";
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), role);
        publishAfterCommit(() -> userEventPublisher.publishUserRegistered(event),
                "user.registered pour userId=" + user.getId());
    }

    @Transactional
    public void forgotPassword(String email) {
        // Ne révèle pas si l'email existe ou non (privacy)
        userRepository.findByEmail(email).ifPresent(user -> {
            String code = generateSecureCode();
            user.setResetPasswordToken(code);
            user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(15));
            user.setResetTokenVerified(false);
            userRepository.save(user);
            emailService.sendResetCodeEmail(email, code);
            auditLogger.log("FORGOT_PASSWORD", email);
        });
    }

    @Transactional
    public void verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
        if (!code.equals(user.getResetPasswordToken())) {
            auditLogger.logFailure("VERIFY_RESET_CODE", email, "code invalide");
            throw new AppException("Code invalide", HttpStatus.BAD_REQUEST);
        }
        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException("Code expiré", HttpStatus.BAD_REQUEST);
        }
        // Mark as verified so resetPassword knows the code was properly validated
        user.setResetTokenVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new AppException("Token invalide", HttpStatus.BAD_REQUEST));
        if (!user.isResetTokenVerified()) {
            throw new AppException("Code non vérifié, veuillez d'abord valider votre code", HttpStatus.FORBIDDEN);
        }
        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException("Token expiré", HttpStatus.BAD_REQUEST);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new AppException("Le nouveau mot de passe doit être différent de l'ancien", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        user.setResetTokenVerified(false);
        userRepository.save(user);
        // Revoke all refresh tokens on password change
        refreshTokenRepository.revokeAllByUserId(user.getId());
        auditLogger.log("RESET_PASSWORD", user.getEmail());
    }

    @Transactional
    public AuthResponse refreshAccessToken(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(rawRefreshToken)
                .orElseThrow(() -> new AppException("Refresh token invalide", HttpStatus.UNAUTHORIZED));

        if (stored.isRevoked()) {
            // Possible token reuse — revoke all tokens for this user
            refreshTokenRepository.revokeAllByUserId(stored.getUser().getId());
            auditLogger.logFailure("REFRESH_TOKEN", stored.getUser().getEmail(), "token déjà révoqué (possible réutilisation)");
            throw new AppException("Refresh token révoqué", HttpStatus.UNAUTHORIZED);
        }
        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException("Refresh token expiré, veuillez vous reconnecter", HttpStatus.UNAUTHORIZED);
        }

        User user = stored.getUser();
        // Rotate: revoke old token, issue new one
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        String role = (user instanceof Client) ? "CLIENT" : "FREELANCER";
        String newAccessToken = jwtUtil.generateToken(user.getEmail(), role, user.getId());
        String newRefreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .role(role)
                .build();
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenRepository.findByToken(rawRefreshToken).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            auditLogger.log("LOGOUT", token.getUser().getEmail());
        });
    }

    @Transactional
    public void changePassword(String email, org.example.backfreelance.dto.UpdatePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            auditLogger.logFailure("CHANGE_PASSWORD", email, "mot de passe actuel incorrect");
            throw new AppException("Mot de passe actuel incorrect", HttpStatus.BAD_REQUEST);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException("Le nouveau mot de passe doit être différent de l'ancien", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenRepository.revokeAllByUserId(user.getId());
        auditLogger.log("CHANGE_PASSWORD", email);
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
        Long userId = user.getId();
        // Supprimer tous les refresh tokens AVANT de supprimer l'utilisateur
        // (sinon violation de contrainte FK : refresh_tokens.user_id référence users.id)
        refreshTokenRepository.deleteAllByUserId(userId);
        userRepository.delete(user);
        auditLogger.log("DELETE_ACCOUNT", email);

        // Publier APRÈS le commit — ne pas bloquer la suppression si RabbitMQ est down
        UserDeletedEvent event = new UserDeletedEvent(userId, email);
        publishAfterCommit(() -> userEventPublisher.publishUserDeleted(event),
                "user.deleted pour userId=" + userId);
    }

    private String createRefreshToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenValue;
    }

    private String generateSecureCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredAndRevoked(LocalDateTime.now());
        log.info("[Scheduler] Nettoyage des refresh tokens expirés effectué");
    }

    /**
     * Enregistre une action à exécuter APRÈS le commit de la transaction courante.
     * Garantit que l'échec de RabbitMQ ne rollback pas la transaction métier.
     */
    private void publishAfterCommit(Runnable action, String label) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    action.run();
                } catch (Exception e) {
                    log.warn("[RabbitMQ] Échec de publication {} : {}. Le profil/wallet sera créé à la prochaine connexion.", label, e.getMessage());
                }
            }
        });
    }
}