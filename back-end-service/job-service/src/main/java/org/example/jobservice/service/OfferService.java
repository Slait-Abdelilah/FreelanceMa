package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.CreateOfferRequest;
import org.example.jobservice.dto.OfferDTO;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.BudgetType;
import org.example.jobservice.enums.OfferCategory;
import org.example.jobservice.enums.OfferStatus;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    public Page<OfferDTO> getAllOpenOffers(Pageable pageable) {
        return offerRepository
                .findByStatusOrderByCreatedAtDesc(OfferStatus.OPEN, pageable)
                .map(this::toDTO);
    }

    public Page<OfferDTO> searchOffers(String keyword, Pageable pageable) {
        return offerRepository.searchOffers(keyword, pageable).map(this::toDTO);
    }

    public Page<OfferDTO> filterByCategory(String category, Pageable pageable) {
        try {
            OfferCategory cat = OfferCategory.valueOf(category.toUpperCase());
            return offerRepository
                    .findByStatusAndCategoryOrderByCreatedAtDesc(OfferStatus.OPEN, cat, pageable)
                    .map(this::toDTO);
        } catch (IllegalArgumentException e) {
            return offerRepository
                    .findByStatusOrderByCreatedAtDesc(OfferStatus.OPEN, pageable)
                    .map(this::toDTO);
        }
    }

    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        return toDTO(offer);
    }

    public OfferDTO createOffer(Long clientId, CreateOfferRequest request) {
        Offer offer = Offer.builder()
                .clientId(clientId)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory() != null
                        ? OfferCategory.valueOf(request.getCategory().toUpperCase())
                        : OfferCategory.OTHER)
                .budgetMin(request.getBudgetMin())
                .budgetMax(request.getBudgetMax())
                .budgetType(request.getBudgetType() != null
                        ? BudgetType.valueOf(request.getBudgetType())
                        : BudgetType.FIXED)
                .deadline(request.getDeadline())
                .requiredSkills(request.getRequiredSkills())
                .status(OfferStatus.OPEN)
                .build();

        return toDTO(offerRepository.save(offer));
    }

    private OfferDTO toDTO(Offer o) {
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