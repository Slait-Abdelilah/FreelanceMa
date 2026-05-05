package org.example.jobservice.repository;

import org.example.jobservice.entity.Application;
import org.example.jobservice.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // candidatures d'un freelancer
    List<Application> findByFreelancerIdOrderByCreatedAtDesc(Long freelancerId);

    // candidatures pour une offre
    List<Application> findByOfferIdOrderByCreatedAtDesc(Long offerId);

    // vérifier si un freelancer a déjà postulé
    boolean existsByOfferIdAndFreelancerId(Long offerId, Long freelancerId);

    // candidatures par statut
    List<Application> findByFreelancerIdAndStatusOrderByCreatedAtDesc(
            Long freelancerId, ApplicationStatus status
    );
}

