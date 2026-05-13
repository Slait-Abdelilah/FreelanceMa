package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.client.ProfileClient;
import org.example.jobservice.dto.CreateReviewRequest;
import org.example.jobservice.dto.ReviewDTO;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.entity.Review;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.enums.NotificationType;
import org.example.jobservice.exception.ConflictException;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.OfferRepository;
import org.example.jobservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;
    private final NotificationService notificationService;
    private final ProfileClient profileClient;

    @Transactional
    public ReviewDTO submitReview(Long reviewerId, String reviewerRole, CreateReviewRequest request) {
        Application app = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        if (app.getStatus() != ApplicationStatus.COMPLETED) {
            throw new ConflictException("Vous ne pouvez noter qu'une mission terminée");
        }

        // Determine who is being reviewed
        Long reviewedId;
        if ("CLIENT".equals(reviewerRole)) {
            // Only the client of this offer can review the freelancer
            Offer offer = offerRepository.findById(app.getOfferId())
                    .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
            if (!offer.getClientId().equals(reviewerId)) {
                throw new ForbiddenException("Accès refusé");
            }
            reviewedId = app.getFreelancerId();
        } else {
            // Freelancer reviews the client
            if (!app.getFreelancerId().equals(reviewerId)) {
                throw new ForbiddenException("Accès refusé");
            }
            Offer offer = offerRepository.findById(app.getOfferId())
                    .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
            reviewedId = offer.getClientId();
        }

        if (reviewRepository.findByApplicationIdAndReviewerRole(request.getApplicationId(), reviewerRole).isPresent()) {
            throw new ConflictException("Vous avez déjà laissé un avis pour cette mission");
        }

        Review review = Review.builder()
                .applicationId(request.getApplicationId())
                .reviewerId(reviewerId)
                .reviewedId(reviewedId)
                .reviewerRole(reviewerRole)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review saved = reviewRepository.save(review);

        // Update the reviewed user's average rating
        updateUserRating(reviewedId);

        // Notify the reviewed user
        Offer offer = offerRepository.findById(app.getOfferId()).orElse(null);
        notificationService.create(
                reviewedId,
                NotificationType.REVIEW_RECEIVED,
                "Nouvel avis reçu",
                "Vous avez reçu un avis " + request.getRating() + "/5 pour la mission"
                        + (offer != null ? " « " + offer.getTitle() + " »" : "") + ".",
                app.getOfferId(), app.getId()
        );

        return toDTO(saved);
    }

    public List<ReviewDTO> getFreelancerReviews(Long freelancerId) {
        return reviewRepository
                .findByReviewedIdAndReviewerRoleOrderByCreatedAtDesc(freelancerId, "CLIENT")
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReviewDTO> getClientReviews(Long clientId) {
        return reviewRepository
                .findByReviewedIdAndReviewerRoleOrderByCreatedAtDesc(clientId, "FREELANCER")
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void updateUserRating(Long userId) {
        List<Review> reviews = reviewRepository.findByReviewedIdOrderByCreatedAtDesc(userId);
        if (reviews.isEmpty()) return;

        OptionalDouble avg = reviews.stream().mapToInt(Review::getRating).average();
        double average = avg.orElse(0.0);
        double rounded = Math.round(average * 10.0) / 10.0;

        try {
            profileClient.updateRating(userId, rounded, reviews.size());
        } catch (Exception e) {
            log.warn("Failed to update rating for user {}: {}", userId, e.getMessage());
        }
    }

    private ReviewDTO toDTO(Review r) {
        return ReviewDTO.builder()
                .id(r.getId())
                .applicationId(r.getApplicationId())
                .reviewerId(r.getReviewerId())
                .reviewedId(r.getReviewedId())
                .reviewerRole(r.getReviewerRole())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
