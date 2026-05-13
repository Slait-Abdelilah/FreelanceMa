package org.example.jobservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long applicationId;
    private Long reviewerId;
    private Long reviewedId;
    private String reviewerRole;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
