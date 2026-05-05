package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.ApplicationDTO;
import org.example.jobservice.dto.CreateApplicationRequest;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.enums.OfferStatus;
import org.example.jobservice.exception.ConflictException;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;

    @Transactional
    public ApplicationDTO apply(Long freelancerId, CreateApplicationRequest request) {
        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        if (offer.getStatus() != OfferStatus.OPEN) {
            throw new ConflictException("Cette offre n'est plus disponible");
        }

        if (applicationRepository.existsByOfferIdAndFreelancerId(request.getOfferId(), freelancerId)) {
            throw new ConflictException("Vous avez déjà postulé à cette offre");
        }

        Application application = Application.builder()
                .offerId(request.getOfferId())
                .freelancerId(freelancerId)
                .coverLetter(request.getCoverLetter())
                .proposedBudget(request.getProposedBudget())
                .proposedDays(request.getProposedDays())
                .status(ApplicationStatus.PENDING)
                .build();

        offer.setApplicationsCount(offer.getApplicationsCount() + 1);
        offerRepository.save(offer);

        return toDTO(applicationRepository.save(application), offer.getTitle());
    }

    public List<ApplicationDTO> getMyApplications(Long freelancerId) {
        return applicationRepository
                .findByFreelancerIdOrderByCreatedAtDesc(freelancerId)
                .stream()
                .map(app -> {
                    Offer offer = offerRepository.findById(app.getOfferId()).orElse(null);
                    return ApplicationDTO.builder()
                            .id(app.getId())
                            .offerId(app.getOfferId())
                            .freelancerId(app.getFreelancerId())
                            .coverLetter(app.getCoverLetter())
                            .proposedBudget(app.getProposedBudget())
                            .proposedDays(app.getProposedDays())
                            .status(app.getStatus().name())
                            .createdAt(app.getCreatedAt())
                            .offerTitle(offer != null ? offer.getTitle() : "Offre supprimée")
                            .offerCategory(offer != null && offer.getCategory() != null
                                    ? offer.getCategory().name() : null)
                            .offerBudgetMin(offer != null ? offer.getBudgetMin() : null)
                            .offerBudgetMax(offer != null ? offer.getBudgetMax() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ApplicationDTO getApplicationById(Long freelancerId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        if (!app.getFreelancerId().equals(freelancerId)) {
            throw new ForbiddenException("Accès refusé");
        }

        String offerTitle = offerRepository.findById(app.getOfferId())
                .map(Offer::getTitle)
                .orElse("Offre supprimée");

        return toDTO(app, offerTitle);
    }

    @Transactional
    public void withdrawApplication(Long freelancerId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        if (!app.getFreelancerId().equals(freelancerId)) {
            throw new ForbiddenException("Accès refusé");
        }

        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new ConflictException("Impossible de retirer cette candidature");
        }

        app.setStatus(ApplicationStatus.WITHDRAWN);
        applicationRepository.save(app);

        offerRepository.findById(app.getOfferId()).ifPresent(offer -> {
            offer.setApplicationsCount(Math.max(0, offer.getApplicationsCount() - 1));
            offerRepository.save(offer);
        });
    }

    public ApplicationDTO toDTO(Application a, String offerTitle) {
        return ApplicationDTO.builder()
                .id(a.getId())
                .offerId(a.getOfferId())
                .freelancerId(a.getFreelancerId())
                .coverLetter(a.getCoverLetter())
                .proposedBudget(a.getProposedBudget())
                .proposedDays(a.getProposedDays())
                .status(a.getStatus().name())
                .createdAt(a.getCreatedAt())
                .offerTitle(offerTitle)
                .build();
    }
}