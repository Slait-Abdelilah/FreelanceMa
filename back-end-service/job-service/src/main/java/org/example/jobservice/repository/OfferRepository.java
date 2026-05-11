package org.example.jobservice.repository;

import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.OfferCategory;
import org.example.jobservice.enums.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    Page<Offer> findByStatusOrderByCreatedAtDesc(OfferStatus status, Pageable pageable);

    @Query(value = "SELECT o FROM Offer o WHERE o.status = org.example.jobservice.enums.OfferStatus.OPEN AND " +
            "(LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            countQuery = "SELECT COUNT(o) FROM Offer o WHERE o.status = org.example.jobservice.enums.OfferStatus.OPEN AND " +
            "(LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Offer> searchOffers(@Param("keyword") String keyword, Pageable pageable);

    Page<Offer> findByStatusAndCategoryOrderByCreatedAtDesc(
            OfferStatus status, OfferCategory category, Pageable pageable);

    // offres d'un client
    List<Offer> findByClientIdOrderByCreatedAtDesc(Long clientId);

    // filtrer par budget
    @Query("SELECT o FROM Offer o WHERE o.status = org.example.jobservice.enums.OfferStatus.OPEN AND " +
            "o.budgetMax >= :minBudget AND o.budgetMin <= :maxBudget")
    List<Offer> findByBudgetRange(
            @Param("minBudget") BigDecimal minBudget,
            @Param("maxBudget") BigDecimal maxBudget
    );
}