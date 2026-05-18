package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.ProfileDTO;
import org.example.userservice.dto.UpdateProfileRequest;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;

    public ProfileDTO getProfile(String email, Long userId, String role) {
        return toDTO(getOrCreate(email, userId, role));
    }

    @Transactional
    public ProfileDTO updateProfile(String email, Long userId, String role, UpdateProfileRequest req) {
        UserProfile profile = getOrCreate(email, userId, role);

        // Common fields — both roles
        if (req.getFirstName() != null)  profile.setFirstName(req.getFirstName());
        if (req.getLastName() != null)   profile.setLastName(req.getLastName());
        if (req.getBio() != null)        profile.setBio(req.getBio());
        if (req.getLocation() != null)   profile.setLocation(req.getLocation());

        if ("CLIENT".equals(profile.getRole())) {
            if (req.getCompanyName() != null) profile.setCompanyName(req.getCompanyName());
            if (req.getWebsite() != null)     profile.setWebsite(req.getWebsite());
            if (req.getSector() != null)      profile.setSector(req.getSector());
        } else if ("FREELANCER".equals(profile.getRole())) {
            if (req.getTitle() != null)           profile.setTitle(req.getTitle());
            if (req.getSkills() != null)          profile.setSkills(req.getSkills());
            if (req.getHourlyRate() != null)      profile.setHourlyRate(req.getHourlyRate());
            if (req.getIsAvailable() != null)     profile.setIsAvailable(req.getIsAvailable());
            if (req.getPortfolioUrl() != null)    profile.setPortfolioUrl(req.getPortfolioUrl());
            if (req.getGithubUrl() != null)       profile.setGithubUrl(req.getGithubUrl());
            if (req.getLinkedinUrl() != null)     profile.setLinkedinUrl(req.getLinkedinUrl());
            if (req.getExperienceLevel() != null) profile.setExperienceLevel(req.getExperienceLevel());
        }

        return toDTO(userProfileRepository.save(profile));
    }

    public List<ProfileDTO> getAllFreelancers(String search) {
        List<UserProfile> profiles = (search != null && !search.isBlank())
                ? userProfileRepository.searchFreelancers(search.trim())
                : userProfileRepository.findAllByRole("FREELANCER");
        return profiles.stream().map(this::toDTO).toList();
    }

    public ProfileDTO getPublicProfile(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException("Profil introuvable", HttpStatus.NOT_FOUND));
        return toDTO(profile);
    }

    @Transactional
    public void updateRating(Long userId, double averageRating, int totalReviews) {
        userProfileRepository.findById(userId).ifPresent(profile -> {
            profile.setAverageRating(averageRating);
            profile.setTotalReviews(totalReviews);
            userProfileRepository.save(profile);
        });
    }

    @Transactional
    public void incrementCompletedMissions(Long userId) {
        userProfileRepository.findById(userId).ifPresent(profile -> {
            profile.setCompletedMissions(profile.getCompletedMissions() + 1);
            userProfileRepository.save(profile);
        });
    }

    private ProfileDTO toDTO(UserProfile p) {
        return ProfileDTO.builder()
                .id(p.getUserId())
                .email(p.getEmail())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .role(p.getRole())
                .companyName(p.getCompanyName())
                .website(p.getWebsite())
                .sector(p.getSector())
                .title(p.getTitle()).bio(p.getBio()).location(p.getLocation())
                .skills(p.getSkills()).hourlyRate(p.getHourlyRate())
                .experienceLevel(p.getExperienceLevel())
                .isAvailable(p.getIsAvailable())
                .portfolioUrl(p.getPortfolioUrl()).githubUrl(p.getGithubUrl()).linkedinUrl(p.getLinkedinUrl())
                .averageRating(p.getAverageRating()).totalReviews(p.getTotalReviews())
                .completedMissions(p.getCompletedMissions())
                .build();
    }

    private UserProfile getOrCreate(String email, Long userId, String role) {
        return userProfileRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.warn("[ProfileService] Profil introuvable pour userId={} — création d'urgence (événement RabbitMQ manqué ?)", userId);
                    return userProfileRepository.save(
                            UserProfile.builder().userId(userId).email(email).role(role).build()
                    );
                });
    }
}
