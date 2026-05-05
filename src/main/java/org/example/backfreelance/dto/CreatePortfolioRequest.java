package org.example.backfreelance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePortfolioRequest {
    private String title;
    private String description;
    private String technologies;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
}
