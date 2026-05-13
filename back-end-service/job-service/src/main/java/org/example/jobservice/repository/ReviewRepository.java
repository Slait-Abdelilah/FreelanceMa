package org.example.jobservice.repository;

import org.example.jobservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewedIdAndReviewerRoleOrderByCreatedAtDesc(Long reviewedId, String reviewerRole);
    Optional<Review> findByApplicationIdAndReviewerRole(Long applicationId, String reviewerRole);
    List<Review> findByReviewedIdOrderByCreatedAtDesc(Long reviewedId);

    @Query("SELECT r.applicationId FROM Review r WHERE r.reviewerId = :reviewerId")
    List<Long> findApplicationIdsByReviewerId(Long reviewerId);
}
