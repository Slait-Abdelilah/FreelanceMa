package org.example.jobservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8082}", contextId = "profileClient")
public interface ProfileClient {

    @PutMapping("/internal/profile/{userId}/rating")
    void updateRating(@PathVariable Long userId,
                      @RequestParam double averageRating,
                      @RequestParam int totalReviews);

    @PutMapping("/internal/profile/{userId}/completed-missions")
    void incrementCompletedMissions(@PathVariable Long userId);
}
