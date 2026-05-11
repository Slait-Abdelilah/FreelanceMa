package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.ProfileDTO;
import org.example.userservice.dto.UpdateProfileRequest;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (!"FREELANCER".equals(profile.getRole())) {
            throw new AppException("Action réservée aux freelancers", HttpStatus.FORBIDDEN);
        }
        if (req.getTitle() != null)        profile.setTitle(req.getTitle());
        if (req.getBio() != null)          profile.setBio(req.getBio());
        if (req.getLocation() != null)     profile.setLocation(req.getLocation());
        if (req.getSkills() != null)       profile.setSkills(req.getSkills());
        if (req.getHourlyRate() != null)   profile.setHourlyRate(req.getHourlyRate());
        if (req.getIsAvailable() != null)  profile.setIsAvailable(req.getIsAvailable());
        if (req.getPortfolioUrl() != null) profile.setPortfolioUrl(req.getPortfolioUrl());
        if (req.getGithubUrl() != null)    profile.setGithubUrl(req.getGithubUrl());
        if (req.getLinkedinUrl() != null)  profile.setLinkedinUrl(req.getLinkedinUrl());
        if (req.getExperienceLevel() != null) profile.setExperienceLevel(req.getExperienceLevel());

        return toDTO(userProfileRepository.save(profile));
    }

    public ProfileDTO getPublicProfile(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException("Profil introuvable", HttpStatus.NOT_FOUND));
        return toDTO(profile);
    }

    private ProfileDTO toDTO(UserProfile p) {
        return ProfileDTO.builder()
                .id(p.getUserId())
                .email(p.getEmail())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .role(p.getRole())
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
                .orElseGet(() -> userProfileRepository.save(
                        UserProfile.builder().userId(userId).email(email).role(role).build()
                ));
    }
}
