package org.example.jobservice.repository;

import org.example.jobservice.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByFreelancerIdOrderBySavedAtDesc(Long freelancerId);
    Optional<Favorite> findByFreelancerIdAndOfferId(Long freelancerId, Long offerId);
    boolean existsByFreelancerIdAndOfferId(Long freelancerId, Long offerId);
}
