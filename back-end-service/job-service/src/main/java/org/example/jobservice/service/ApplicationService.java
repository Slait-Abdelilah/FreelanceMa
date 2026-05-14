package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.client.ProfileClient;
import org.example.jobservice.client.WalletClient;
import org.example.jobservice.dto.ApplicationDTO;
import org.example.jobservice.dto.CreateApplicationRequest;
import org.example.jobservice.dto.EscrowRequest;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;
    private final NotificationService notificationService;
    private final WalletClient walletClient;
    private final ProfileClient profileClient;

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

    // Freelancer marks work as done → goes to AWAITING_VALIDATION (client must validate)
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

        app.setStatus(ApplicationStatus.AWAITING_VALIDATION);
        app.setCompletedAt(java.time.LocalDateTime.now());
        applicationRepository.save(app);

        Offer offer = offerRepository.findById(app.getOfferId()).orElse(null);
        String offerTitle = offer != null ? offer.getTitle() : "Offre supprimée";

        // Notify freelancer
        notificationService.create(
                freelancerId,
                NotificationType.AWAITING_VALIDATION,
                "En attente de validation",
                "Vous avez marqué la mission « " + offerTitle + " » comme terminée. En attente de validation par le client.",
                app.getOfferId(), applicationId
        );

        // Notify client
        if (offer != null) {
            notificationService.create(
                    offer.getClientId(),
                    NotificationType.AWAITING_VALIDATION,
                    "Mission à valider",
                    "Le freelancer a terminé la mission « " + offerTitle + " ». Veuillez valider le travail.",
                    offer.getId(), applicationId
            );
        }

        return toDTO(app, offerTitle);
    }

    // Client validates the mission → COMPLETED + escrow released to freelancer
    @Transactional
    public ApplicationDTO validateMission(Long clientId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        if (!offer.getClientId().equals(clientId)) {
            throw new ForbiddenException("Accès refusé");
        }

        if (app.getStatus() != ApplicationStatus.AWAITING_VALIDATION) {
            throw new ConflictException("Cette mission n'est pas en attente de validation");
        }

        app.setStatus(ApplicationStatus.COMPLETED);
        applicationRepository.save(app);

        // Release client escrow + credit freelancer directly
        if (app.getProposedBudget() != null) {
            try {
                walletClient.clientEscrowRelease(new EscrowRequest(
                        clientId,
                        app.getProposedBudget(),
                        app.getId(),
                        "Mission validée — paiement libéré pour « " + offer.getTitle() + " »"
                ));
                walletClient.freelancerDirectCredit(new EscrowRequest(
                        app.getFreelancerId(),
                        app.getProposedBudget(),
                        app.getId(),
                        "Paiement reçu pour « " + offer.getTitle() + " »"
                ));
            } catch (Exception e) {
                log.warn("Wallet payment failed for application {}: {}", applicationId, e.getMessage());
            }
        }

        // Increment freelancer completed missions
        try {
            profileClient.incrementCompletedMissions(app.getFreelancerId());
        } catch (Exception e) {
            log.warn("Profile update failed for freelancer {}: {}", app.getFreelancerId(), e.getMessage());
        }

        // Notify freelancer
        notificationService.create(
                app.getFreelancerId(),
                NotificationType.MISSION_VALIDATED,
                "Mission validée !",
                "Le client a validé votre mission « " + offer.getTitle() + " ». Le paiement a été libéré.",
                offer.getId(), applicationId
        );

        // Notify client
        notificationService.create(
                clientId,
                NotificationType.MISSION_VALIDATED,
                "Mission clôturée",
                "Vous avez validé la mission « " + offer.getTitle() + " ». N'oubliez pas de laisser un avis.",
                offer.getId(), applicationId
        );

        return toDTO(app, offer.getTitle(), offer);
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
                .map(app -> toDTO(app, offer.getTitle(), offer))
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

        // Block client funds in escrow — propagates if insufficient balance
        if (app.getProposedBudget() != null) {
            try {
                walletClient.clientEscrowHold(new EscrowRequest(
                        clientId,
                        app.getProposedBudget(),
                        app.getId(),
                        "Fonds bloqués pour « " + offer.getTitle() + " »"
                ));
            } catch (feign.FeignException.BadRequest e) {
                throw new ConflictException("Solde insuffisant pour accepter cette candidature. Rechargez votre wallet.");
            } catch (Exception e) {
                log.warn("Client escrow hold failed for application {} (service unavailable): {}", applicationId, e.getMessage());
            }
        }

        app.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(app);

        notificationService.create(
                app.getFreelancerId(),
                NotificationType.APPLICATION_ACCEPTED,
                "Candidature acceptée",
                "Votre candidature pour « " + offer.getTitle() + " » a été acceptée. Bonne mission !",
                offer.getId(), app.getId()
        );

        return toDTO(app, offer.getTitle(), offer);
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

        return toDTO(app, offer.getTitle(), offer);
    }

    public List<ApplicationDTO> getCompletedWithFreelancer(Long clientId, Long freelancerId) {
        return applicationRepository.findCompletedByClientAndFreelancer(clientId, freelancerId)
                .stream()
                .map(app -> {
                    Offer offer = offerRepository.findById(app.getOfferId()).orElse(null);
                    return toDTO(app, offer != null ? offer.getTitle() : "Offre supprimée", offer);
                })
                .collect(Collectors.toList());
    }

    public ApplicationDTO toDTO(Application a, String offerTitle) {
        return toDTO(a, offerTitle, null);
    }

    public ApplicationDTO toDTO(Application a, String offerTitle, Offer offer) {
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
                .offerCategory(offer != null && offer.getCategory() != null ? offer.getCategory().name() : null)
                .offerBudgetMin(offer != null ? offer.getBudgetMin() : null)
                .offerBudgetMax(offer != null ? offer.getBudgetMax() : null)
                .build();
    }
}
