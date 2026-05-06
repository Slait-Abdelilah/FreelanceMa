package org.example.userservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PortfolioDTO {
    private Long id;
    private String title;
    private String description;
    private String technologies;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
