package org.example.userservice.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreatePortfolioRequest {
    private String title;
    private String description;
    private String technologies;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
}
