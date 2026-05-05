package org.example.backfreelance.service;

import lombok.AllArgsConstructor;
import org.example.backfreelance.dto.ProfileDTO;
import org.example.backfreelance.dto.UpdateProfileRequest;
import org.example.backfreelance.entity.Freelancer;
import org.example.backfreelance.entity.User;
import org.example.backfreelance.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.backfreelance.entity.Client;


@Service
@AllArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    // ===== RÉCUPÉRER LE PROFIL =====
    public ProfileDTO getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        ProfileDTO.ProfileDTOBuilder builder = ProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName());

        if (user instanceof Freelancer freelancer) {
            builder
                    .role("FREELANCER")
                    .title(freelancer.getTitle())
                    .bio(freelancer.getBio())
                    .location(freelancer.getLocation())
                    .skills(freelancer.getSkills())
                    .hourlyRate(freelancer.getHourlyRate())
                    .experienceLevel(
                            freelancer.getExperienceLevel() != null
                                    ? freelancer.getExperienceLevel().name()
                                    : "JUNIOR"
                    )
                    .isAvailable(freelancer.getIsAvailable())
                    .portfolioUrl(freelancer.getPortfolioUrl())
                    .githubUrl(freelancer.getGithubUrl())
                    .linkedinUrl(freelancer.getLinkedinUrl())
                    .averageRating(freelancer.getAverageRating())
                    .totalReviews(freelancer.getTotalReviews())
                    .completedMissions(freelancer.getCompletedMissions());

        } else if (user instanceof Client client) {
            builder
                    .role("CLIENT")
                    .averageRating(client.getAverageRating());
        }

        return builder.build();
    }

    // ===== METTRE À JOUR LE PROFIL FREELANCER =====
    public ProfileDTO updateProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // seulement les freelancers peuvent modifier leur profil ici
        if (!(user instanceof Freelancer freelancer)) {
            throw new RuntimeException("Action réservée aux freelancers");
        }

        // mise à jour uniquement si la valeur est envoyée
        if (request.getTitle() != null)
            freelancer.setTitle(request.getTitle());

        if (request.getBio() != null)
            freelancer.setBio(request.getBio());

        if (request.getLocation() != null)
            freelancer.setLocation(request.getLocation());

        if (request.getSkills() != null)
            freelancer.setSkills(request.getSkills());

        if (request.getHourlyRate() != null)
            freelancer.setHourlyRate(request.getHourlyRate());

        if (request.getExperienceLevel() != null) {
            try {
                freelancer.setExperienceLevel(
                        org.example.backfreelance.enums.ExperienceLevel
                                .valueOf(request.getExperienceLevel())
                );
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Niveau d'expérience invalide : " + request.getExperienceLevel());
            }
        }

        if (request.getIsAvailable() != null)
            freelancer.setIsAvailable(request.getIsAvailable());

        if (request.getPortfolioUrl() != null)
            freelancer.setPortfolioUrl(request.getPortfolioUrl());

        if (request.getGithubUrl() != null)
            freelancer.setGithubUrl(request.getGithubUrl());

        if (request.getLinkedinUrl() != null)
            freelancer.setLinkedinUrl(request.getLinkedinUrl());

        userRepository.save(freelancer);
        return getProfile(email);
    }

    // ===== RÉCUPÉRER UN PROFIL PUBLIC PAR ID =====
    // utilisé par les clients pour voir le profil d'un freelancer
    public ProfileDTO getPublicProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        return getProfile(user.getEmail());
    }
}
