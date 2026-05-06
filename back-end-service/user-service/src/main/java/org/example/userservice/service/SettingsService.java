package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.*;
import org.example.userservice.entity.Client;
import org.example.userservice.entity.Freelancer;
import org.example.userservice.entity.User;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (user instanceof Freelancer f) {
            builder.publicProfile(f.getPublicProfile()).showEarnings(f.getShowEarnings()).role("FREELANCER");
        } else if (user instanceof Client c) {
            builder.publicProfile(c.getPublicProfile()).showEarnings(false).role("CLIENT");
        }
        return builder.build();
    }

    @Transactional
    public UserSettingsDTO updateAccount(String email, UpdateAccountRequest request) {
        User user = getUser(email);
        if (request.getPhone() != null)    user.setPhone(request.getPhone());
        if (request.getLanguage() != null) user.setLanguage(request.getLanguage());
        if (request.getCurrency() != null) user.setCurrency(request.getCurrency());
        userRepository.save(user);
        return getSettings(email);
    }

    @Transactional
    public void updatePassword(String email, UpdatePasswordRequest request) {
        User user = getUser(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException("Mot de passe actuel incorrect", HttpStatus.BAD_REQUEST);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException("Le nouveau mot de passe doit être différent", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserSettingsDTO updatePrivacy(String email, UpdatePrivacyRequest request) {
        User user = getUser(email);
        if (request.getShowOnlineStatus() != null) user.setShowOnlineStatus(request.getShowOnlineStatus());
        if (user instanceof Freelancer f) {
            if (request.getPublicProfile() != null) f.setPublicProfile(request.getPublicProfile());
            if (request.getShowEarnings() != null)  f.setShowEarnings(request.getShowEarnings());
        } else if (user instanceof Client c) {
            if (request.getPublicProfile() != null) c.setPublicProfile(request.getPublicProfile());
        }
        userRepository.save(user);
        return getSettings(email);
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = getUser(email);
        userRepository.delete(user);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
    }
}
