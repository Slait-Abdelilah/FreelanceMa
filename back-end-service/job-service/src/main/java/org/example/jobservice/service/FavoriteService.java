package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.FavoriteDTO;
import org.example.jobservice.entity.Favorite;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.FavoriteRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final OfferRepository offerRepository;

    public List<FavoriteDTO> getMyFavorites(Long freelancerId) {
        return favoriteRepository.findByFreelancerIdOrderBySavedAtDesc(freelancerId)
                .stream()
                .map(fav -> {
                    Offer offer = offerRepository.findById(fav.getOfferId()).orElse(null);
                    return toDTO(fav, offer);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> toggle(Long freelancerId, Long offerId) {
        offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        return favoriteRepository.findByFreelancerIdAndOfferId(freelancerId, offerId)
                .map(existing -> {
                    favoriteRepository.delete(existing);
                    return Map.<String, Object>of("saved", false);
                })
                .orElseGet(() -> {
                    favoriteRepository.save(
                            Favorite.builder()
                                    .freelancerId(freelancerId)
                                    .offerId(offerId)
                                    .build()
                    );
                    return Map.<String, Object>of("saved", true);
                });
    }

    public boolean isSaved(Long freelancerId, Long offerId) {
        return favoriteRepository.existsByFreelancerIdAndOfferId(freelancerId, offerId);
    }

    private FavoriteDTO toDTO(Favorite fav, Offer offer) {
        FavoriteDTO.FavoriteDTOBuilder b = FavoriteDTO.builder()
                .favoriteId(fav.getId())
                .savedAt(fav.getSavedAt())
                .offerId(fav.getOfferId());

        if (offer != null) {
            b.title(offer.getTitle())
             .description(offer.getDescription())
             .category(offer.getCategory() != null ? offer.getCategory().name() : null)
             .budgetMin(offer.getBudgetMin())
             .budgetMax(offer.getBudgetMax())
             .budgetType(offer.getBudgetType() != null ? offer.getBudgetType().name() : null)
             .deadline(offer.getDeadline())
             .requiredSkills(offer.getRequiredSkills())
             .status(offer.getStatus().name())
             .applicationsCount(offer.getApplicationsCount())
             .offerCreatedAt(offer.getCreatedAt());
        } else {
            b.title("Offre supprimée").status("DELETED");
        }

        return b.build();
    }
}
