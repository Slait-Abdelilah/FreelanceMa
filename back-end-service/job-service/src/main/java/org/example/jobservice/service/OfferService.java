package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.CreateOfferRequest;
import org.example.jobservice.dto.OfferDTO;
import org.example.jobservice.dto.UpdateOfferRequest;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.BudgetType;
import org.example.jobservice.enums.OfferCategory;
import org.example.jobservice.enums.OfferStatus;
import org.example.jobservice.exception.ConflictException;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final ApplicationRepository applicationRepository;

    // ── PUBLIC ─────────────────────────────────────────────────────────────

    public Page<OfferDTO> getAllOpenOffers(Pageable pageable) {
        return offerRepository.findByStatusOrderByCreatedAtDesc(OfferStatus.OPEN, pageable).map(this::toDTO);
    }

    public Page<OfferDTO> searchOffers(String keyword, Pageable pageable) {
        return offerRepository.searchOffers(keyword, pageable).map(this::toDTO);
    }

    public Page<OfferDTO> filterByCategory(String category, Pageable pageable) {
        try {
            OfferCategory cat = OfferCategory.valueOf(category.toUpperCase());
            return offerRepository.findByStatusAndCategoryOrderByCreatedAtDesc(OfferStatus.OPEN, cat, pageable).map(this::toDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Catégorie invalide : « " + category + " ». Valeurs acceptées : "
                    + java.util.Arrays.toString(OfferCategory.values()));
        }
    }

    public OfferDTO getOfferById(Long id) {
        return toDTO(offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable")));
    }

    // ── CLIENT ─────────────────────────────────────────────────────────────

    public OfferDTO createOffer(Long clientId, CreateOfferRequest req) {
        Offer offer = Offer.builder()
                .clientId(clientId)
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory() != null ? OfferCategory.valueOf(req.getCategory().toUpperCase()) : OfferCategory.OTHER)
                .budgetMin(req.getBudgetMin())
                .budgetMax(req.getBudgetMax())
                .budgetType(req.getBudgetType() != null ? BudgetType.valueOf(req.getBudgetType()) : BudgetType.FIXED)
                .deadline(req.getDeadline())
                .requiredSkills(req.getRequiredSkills())
                .status(OfferStatus.OPEN)
                .build();
        return toDTO(offerRepository.save(offer));
    }

    public List<OfferDTO> getMyOffers(Long clientId) {
        return offerRepository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public OfferDTO updateOffer(Long clientId, Long offerId, UpdateOfferRequest req) {
        Offer offer = findOwned(clientId, offerId);
        if (offer.getStatus() == OfferStatus.CLOSED)
            throw new ConflictException("Une offre clôturée ne peut pas être modifiée");

        if (req.getTitle() != null)         offer.setTitle(req.getTitle());
        if (req.getDescription() != null)   offer.setDescription(req.getDescription());
        if (req.getCategory() != null)      offer.setCategory(OfferCategory.valueOf(req.getCategory().toUpperCase()));
        if (req.getBudgetMin() != null)     offer.setBudgetMin(req.getBudgetMin());
        if (req.getBudgetMax() != null)     offer.setBudgetMax(req.getBudgetMax());
        if (req.getBudgetType() != null)    offer.setBudgetType(BudgetType.valueOf(req.getBudgetType()));
        if (req.getDeadline() != null)      offer.setDeadline(req.getDeadline());
        if (req.getRequiredSkills() != null) offer.setRequiredSkills(req.getRequiredSkills());

        return toDTO(offerRepository.save(offer));
    }

    @Transactional
    public OfferDTO closeOffer(Long clientId, Long offerId) {
        Offer offer = findOwned(clientId, offerId);
        if (offer.getStatus() == OfferStatus.CLOSED)
            throw new ConflictException("L'offre est déjà clôturée");
        offer.setStatus(OfferStatus.CLOSED);
        return toDTO(offerRepository.save(offer));
    }

    @Transactional
    public void deleteOffer(Long clientId, Long offerId) {
        Offer offer = findOwned(clientId, offerId);
        if (applicationRepository.existsByOfferId(offerId))
            throw new ConflictException("Impossible de supprimer une offre ayant des candidatures");
        offerRepository.delete(offer);
    }

    // ── PRIVATE ────────────────────────────────────────────────────────────

    private Offer findOwned(Long clientId, Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offer.getClientId().equals(clientId))
            throw new ForbiddenException("Accès refusé");
        return offer;
    }

    public OfferDTO toDTO(Offer o) {
        return OfferDTO.builder()
                .id(o.getId())
                .clientId(o.getClientId())
                .title(o.getTitle())
                .description(o.getDescription())
                .category(o.getCategory() != null ? o.getCategory().name() : null)
                .budgetMin(o.getBudgetMin())
                .budgetMax(o.getBudgetMax())
                .budgetType(o.getBudgetType() != null ? o.getBudgetType().name() : null)
                .deadline(o.getDeadline())
                .requiredSkills(o.getRequiredSkills())
                .status(o.getStatus().name())
                .applicationsCount(o.getApplicationsCount())
                .createdAt(o.getCreatedAt())
                .build();
    }
}
