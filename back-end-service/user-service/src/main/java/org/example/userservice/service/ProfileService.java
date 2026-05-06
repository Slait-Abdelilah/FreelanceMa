package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.ProfileDTO;
import org.example.userservice.dto.UpdateProfileRequest;
import org.example.userservice.entity.Client;
import org.example.userservice.entity.Freelancer;
import org.example.userservice.entity.User;
import org.example.userservice.enums.ExperienceLevel;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileDTO getProfile(String email) {
        User user = getUser(email);
        return toDTO(user);
    }

    @Transactional
    public ProfileDTO updateProfile(String email, UpdateProfileRequest request) {
        User user = getUser(email);
        if (!(user instanceof Freelancer freelancer)) {
            throw new AppException("Action réservée aux freelancers", HttpStatus.FORBIDDEN);
        }
        if (request.getTitle() != null)        freelancer.setTitle(request.getTitle());
        if (request.getBio() != null)          freelancer.setBio(request.getBio());
        if (request.getLocation() != null)     freelancer.setLocation(request.getLocation());
        if (request.getSkills() != null)       freelancer.setSkills(request.getSkills());
        if (request.getHourlyRate() != null)   freelancer.setHourlyRate(request.getHourlyRate());
        if (request.getIsAvailable() != null)  freelancer.setIsAvailable(request.getIsAvailable());
        if (request.getPortfolioUrl() != null) freelancer.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getGithubUrl() != null)    freelancer.setGithubUrl(request.getGithubUrl());
        if (request.getLinkedinUrl() != null)  freelancer.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getExperienceLevel() != null) {
            try {
                freelancer.setExperienceLevel(ExperienceLevel.valueOf(request.getExperienceLevel()));
            } catch (IllegalArgumentException e) {
                throw new AppException("Niveau d'expérience invalide", HttpStatus.BAD_REQUEST);
            }
        }
        userRepository.save(freelancer);
        return toDTO(freelancer);
    }

    public ProfileDTO getPublicProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("Profil introuvable", HttpStatus.NOT_FOUND));
        return toDTO(user);
    }

    private ProfileDTO toDTO(User user) {
        ProfileDTO.ProfileDTOBuilder builder = ProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName());

        if (user instanceof Freelancer f) {
            builder.role("FREELANCER")
                    .title(f.getTitle()).bio(f.getBio()).location(f.getLocation())
                    .skills(f.getSkills()).hourlyRate(f.getHourlyRate())
                    .experienceLevel(f.getExperienceLevel() != null ? f.getExperienceLevel().name() : "JUNIOR")
                    .isAvailable(f.getIsAvailable())
                    .portfolioUrl(f.getPortfolioUrl()).githubUrl(f.getGithubUrl()).linkedinUrl(f.getLinkedinUrl())
                    .averageRating(f.getAverageRating()).totalReviews(f.getTotalReviews())
                    .completedMissions(f.getCompletedMissions());
        } else if (user instanceof Client c) {
            builder.role("CLIENT").averageRating(c.getAverageRating());
        }
        return builder.build();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
    }
}
