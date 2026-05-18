package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.*;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserProfileRepository userProfileRepository;

    public UserSettingsDTO getSettings(String email, Long userId, String role) {
        return toDTO(getOrCreate(email, userId, role));
    }

    @Transactional
    public UserSettingsDTO updateAccount(String email, Long userId, String role, UpdateAccountRequest req) {
        UserProfile profile = getOrCreate(email, userId, role);
        if (req.getPhone() != null)    profile.setPhone(req.getPhone());
        if (req.getLanguage() != null) profile.setLanguage(req.getLanguage());
        if (req.getCurrency() != null) profile.setCurrency(req.getCurrency());
        return toDTO(userProfileRepository.save(profile));
    }

    @Transactional
    public UserSettingsDTO updatePrivacy(String email, Long userId, String role, UpdatePrivacyRequest req) {
        UserProfile profile = getOrCreate(email, userId, role);
        if (req.getShowOnlineStatus() != null) profile.setShowOnlineStatus(req.getShowOnlineStatus());
        if (req.getPublicProfile() != null)    profile.setPublicProfile(req.getPublicProfile());
        if (req.getShowEarnings() != null && "FREELANCER".equals(profile.getRole())) {
            profile.setShowEarnings(req.getShowEarnings());
        }
        return toDTO(userProfileRepository.save(profile));
    }

    private UserSettingsDTO toDTO(UserProfile p) {
        return UserSettingsDTO.builder()
                .email(p.getEmail())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .phone(p.getPhone())
                .language(p.getLanguage())
                .currency(p.getCurrency())
                .showOnlineStatus(p.getShowOnlineStatus())
                .publicProfile(p.getPublicProfile())
                .showEarnings("FREELANCER".equals(p.getRole()) ? p.getShowEarnings() : false)
                .role(p.getRole())
                .build();
    }

    private UserProfile getOrCreate(String email, Long userId, String role) {
        return userProfileRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.warn("[SettingsService] Profil introuvable pour userId={} — création d'urgence (événement RabbitMQ manqué ?)", userId);
                    return userProfileRepository.save(
                            UserProfile.builder().userId(userId).email(email).role(role).build()
                    );
                });
    }
}
