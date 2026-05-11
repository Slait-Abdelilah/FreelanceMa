package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.ApplicationDTO;
import org.example.jobservice.dto.CreateApplicationRequest;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.enums.NotificationType;
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
    private final NotificationService notificationService;

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

        Application saved = applicationRepository.save(application);

        notificationService.create(
                freelancerId,
                NotificationType.APPLICATION_SUBMITTED,
                "Candidature envoyée",
                "Votre candidature pour « " + offer.getTitle() + " » a bien été soumise.",
                offer.getId(), saved.getId()
        );

        return toDTO(saved, offer.getTitle());
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
                            .completedAt(app.getCompletedAt())
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
    public ApplicationDTO completeApplication(Long freelancerId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        if (!app.getFreelancerId().equals(freelancerId)) {
            throw new ForbiddenException("Accès refusé");
        }

        if (app.getStatus() != ApplicationStatus.ACCEPTED) {
            throw new ConflictException("Seules les missions acceptées peuvent être marquées comme terminées");
        }

        app.setStatus(ApplicationStatus.COMPLETED);
        app.setCompletedAt(java.time.LocalDateTime.now());
        applicationRepository.save(app);

        String offerTitle = offerRepository.findById(app.getOfferId())
                .map(Offer::getTitle).orElse("Offre supprimée");

        notificationService.create(
                freelancerId,
                NotificationType.MISSION_COMPLETED,
                "Mission terminée",
                "La mission « " + offerTitle + " » a été marquée comme terminée. Félicitations !",
                app.getOfferId(), applicationId
        );

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

            notificationService.create(
                    freelancerId,
                    NotificationType.APPLICATION_WITHDRAWN,
                    "Candidature retirée",
                    "Vous avez retiré votre candidature pour « " + offer.getTitle() + " ».",
                    offer.getId(), applicationId
            );
        });
    }

    public List<ApplicationDTO> getApplicationsForOffer(Long clientId, Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offer.getClientId().equals(clientId))
            throw new ForbiddenException("Accès refusé");

        return applicationRepository.findByOfferIdOrderByCreatedAtDesc(offerId)
                .stream()
                .map(app -> toDTO(app, offer.getTitle()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationDTO acceptApplication(Long clientId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offer.getClientId().equals(clientId))
            throw new ForbiddenException("Accès refusé");
        if (app.getStatus() != ApplicationStatus.PENDING)
            throw new ConflictException("Seules les candidatures en attente peuvent être acceptées");

        app.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(app);

        notificationService.create(
                app.getFreelancerId(),
                NotificationType.APPLICATION_ACCEPTED,
                "Candidature acceptée",
                "Votre candidature pour « " + offer.getTitle() + " » a été acceptée. Bonne mission !",
                offer.getId(), app.getId()
        );

        return toDTO(app, offer.getTitle());
    }

    @Transactional
    public ApplicationDTO rejectApplication(Long clientId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offer.getClientId().equals(clientId))
            throw new ForbiddenException("Accès refusé");
        if (app.getStatus() != ApplicationStatus.PENDING)
            throw new ConflictException("Seules les candidatures en attente peuvent être refusées");

        app.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(app);

        notificationService.create(
                app.getFreelancerId(),
                NotificationType.APPLICATION_REJECTED,
                "Candidature non retenue",
                "Votre candidature pour « " + offer.getTitle() + " » n'a pas été retenue cette fois.",
                offer.getId(), app.getId()
        );

        return toDTO(app, offer.getTitle());
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
                .completedAt(a.getCompletedAt())
                .offerTitle(offerTitle)
                .build();
    }
}