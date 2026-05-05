package org.example.backfreelance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
