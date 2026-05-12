package org.example.userservice.repository;

import org.example.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByEmail(String email);
    void deleteByUserId(Long userId);

    @Query("SELECT p FROM UserProfile p WHERE p.role = 'FREELANCER' AND (" +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.title)     LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.skills)    LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.location)  LIKE LOWER(CONCAT('%', :q, '%')))")
    List<UserProfile> searchFreelancers(@Param("q") String query);

    List<UserProfile> findAllByRole(String role);
}
