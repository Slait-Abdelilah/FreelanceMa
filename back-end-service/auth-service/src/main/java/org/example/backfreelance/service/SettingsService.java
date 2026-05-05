package org.example.backfreelance.service;

import lombok.RequiredArgsConstructor;
import org.example.backfreelance.dto.*;
import org.example.backfreelance.entity.Client;
import org.example.backfreelance.entity.Freelancer;
import org.example.backfreelance.entity.User;
import org.example.backfreelance.enums.AccountStatus;
import org.example.backfreelance.exceptionn.AppException;
import org.example.backfreelance.repository.UserRepository;
import org.example.backfreelance.seecuriity.AuditLogger;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogger auditLogger;

    public UserSettingsDTO getSettings(String email) {
        User user = getUser(email);
        UserSettingsDTO.UserSettingsDTOBuilder builder = UserSettingsDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .language(user.getLanguage())
                .currency(user.getCurrency())
                .showOnlineStatus(user.getShowOnlineStatus());
        if (user instanceof Freelancer freelancer) {
            builder.publicProfile(freelancer.getPublicProfile())
                   .showEarnings(freelancer.getShowEarnings())
                   .role("FREELANCER");
        } else if (user instanceof Client client) {
            builder.publicProfile(client.getPublicProfile())
                   .showEarnings(false)
                   .role("CLIENT");
        }
        return builder.build();
    }

    public UserSettingsDTO updateAccount(String email, UpdateAccountRequest request) {
        User user = getUser(email);
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getLanguage() != null) user.setLanguage(request.getLanguage());
        if (request.getCurrency() != null) user.setCurrency(request.getCurrency());
        userRepository.save(user);
        auditLogger.log("UPDATE_ACCOUNT", email);
        return getSettings(email);
    }

    public void updatePassword(String email, UpdatePasswordRequest request) {
        User user = getUser(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            auditLogger.logFailure("CHANGE_PASSWORD", email, "mot de passe actuel incorrect");
            throw new AppException("Mot de passe actuel incorrect", HttpStatus.BAD_REQUEST);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException("Le nouveau mot de passe doit être différent", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        auditLogger.log("CHANGE_PASSWORD", email);
    }

    public UserSettingsDTO updatePrivacy(String email, UpdatePrivacyRequest request) {
        User user = getUser(email);
        if (request.getShowOnlineStatus() != null) {
            user.setShowOnlineStatus(request.getShowOnlineStatus());
        }
        if (user instanceof Freelancer freelancer) {
            if (request.getPublicProfile() != null) freelancer.setPublicProfile(request.getPublicProfile());
            if (request.getShowEarnings() != null) freelancer.setShowEarnings(request.getShowEarnings());
        } else if (user instanceof Client client) {
            if (request.getPublicProfile() != null) client.setPublicProfile(request.getPublicProfile());
        }
        userRepository.save(user);
        return getSettings(email);
    }

    public void deactivateAccount(String email) {
        User user = getUser(email);
        user.setAccountStatus(AccountStatus.SUSPENDED);
        userRepository.save(user);
        auditLogger.log("DEACTIVATE_ACCOUNT", email);
    }

    public void deleteAccount(String email) {
        User user = getUser(email);
        userRepository.delete(user);
        auditLogger.log("DELETE_ACCOUNT", email);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
    }
}