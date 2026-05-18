package org.example.jobservice.repository;

import org.example.jobservice.entity.Application;
import org.example.jobservice.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // candidatures d'un freelancer
    List<Application> findByFreelancerIdOrderByCreatedAtDesc(Long freelancerId);

    // candidatures pour une offre
    List<Application> findByOfferIdOrderByCreatedAtDesc(Long offerId);

    // vérifier si un freelancer a déjà postulé
    boolean existsByOfferIdAndFreelancerId(Long offerId, Long freelancerId);

    // vérifier si une offre a des candidatures
    boolean existsByOfferId(Long offerId);

    // candidatures par statut
    List<Application> findByFreelancerIdAndStatusOrderByCreatedAtDesc(
            Long freelancerId, ApplicationStatus status
    );

    // candidatures pour plusieurs offres (utilisé pour le compteur messages non-lus côté CLIENT)
    List<Application> findByOfferIdIn(List<Long> offerIds);

    // missions complétées entre un client et un freelancer donné
    @Query("SELECT a FROM Application a WHERE a.freelancerId = :freelancerId " +
           "AND a.status = org.example.jobservice.enums.ApplicationStatus.COMPLETED " +
           "AND a.offerId IN (SELECT o.id FROM Offer o WHERE o.clientId = :clientId)")
    List<Application> findCompletedByClientAndFreelancer(@Param("clientId") Long clientId, @Param("freelancerId") Long freelancerId);
}

