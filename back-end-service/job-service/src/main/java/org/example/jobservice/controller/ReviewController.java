package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.CreateReviewRequest;
import org.example.jobservice.dto.ReviewDTO;
import org.example.jobservice.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> submit(Authentication auth,
                                            @Valid @RequestBody CreateReviewRequest request) {
        Long reviewerId = (Long) auth.getDetails();
        String role = auth.getAuthorities().stream()
                .findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("");
        return ResponseEntity.status(201).body(reviewService.submitReview(reviewerId, role, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Long>> getMyReviewedApplicationIds(Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return ResponseEntity.ok(reviewService.getMyReviewedApplicationIds(userId));
    }

    @GetMapping("/freelancer/{id}")
    public ResponseEntity<List<ReviewDTO>> getFreelancerReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getFreelancerReviews(id));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<ReviewDTO>> getClientReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getClientReviews(id));
    }
}
