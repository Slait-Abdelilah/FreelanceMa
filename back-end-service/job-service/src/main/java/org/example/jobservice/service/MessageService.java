package org.example.jobservice.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.dto.ConversationTokenDTO;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.exception.ConflictException;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;

    private static final Set<ApplicationStatus> ALLOWED_STATUSES = Set.of(
            ApplicationStatus.ACCEPTED,
            ApplicationStatus.AWAITING_VALIDATION,
            ApplicationStatus.COMPLETED
    );

    public ConversationTokenDTO getConversationToken(Long userId, String userRole, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        boolean isClient     = "CLIENT".equals(userRole) && offer.getClientId().equals(userId);
        boolean isFreelancer = "FREELANCER".equals(userRole) && app.getFreelancerId().equals(userId);

        if (!isClient && !isFreelancer) {
            throw new ForbiddenException("Vous n'êtes pas participant de cette mission");
        }

        if (!ALLOWED_STATUSES.contains(app.getStatus())) {
            throw new ConflictException("La messagerie est disponible uniquement pour les missions acceptées");
        }

        String conversationId = "conv_" + applicationId;

        // Créer ou mettre à jour le document de conversation dans Firestore (via Admin SDK)
        ensureConversationExists(conversationId, offer, app);

        String firebaseToken = generateFirebaseToken(userId);

        Long   otherUserId = isClient ? app.getFreelancerId() : offer.getClientId();
        String otherRole   = isClient ? "FREELANCER" : "CLIENT";

        return ConversationTokenDTO.builder()
                .firebaseToken(firebaseToken)
                .conversationId(conversationId)
                .otherUserId(otherUserId)
                .otherUserRole(otherRole)
                .offerTitle(offer.getTitle())
                .build();
    }

    public ConversationTokenDTO refreshToken(Long userId) {
        return ConversationTokenDTO.builder()
                .firebaseToken(generateFirebaseToken(userId))
                .build();
    }

    private void ensureConversationExists(String conversationId, Offer offer, Application app) {
        if (FirebaseApp.getApps().isEmpty()) return;
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            Map<String, Object> data = new HashMap<>();
            data.put("clientId",     String.valueOf(offer.getClientId()));
            data.put("freelancerId", String.valueOf(app.getFreelancerId()));
            data.put("offerTitle",   offer.getTitle());
            data.put("applicationId", app.getId());
            // merge = ne pas écraser les messages existants
            firestore.collection("conversations").document(conversationId).set(data, SetOptions.merge());
        } catch (Exception e) {
            log.warn("Could not create Firestore conversation {}: {}", conversationId, e.getMessage());
        }
    }

    private String generateFirebaseToken(Long userId) {
        if (FirebaseApp.getApps().isEmpty()) {
            throw new ConflictException("Service de messagerie non disponible");
        }
        try {
            return FirebaseAuth.getInstance().createCustomToken(String.valueOf(userId));
        } catch (FirebaseAuthException e) {
            log.error("Failed to create Firebase token for user {}: {}", userId, e.getMessage());
            throw new ConflictException("Impossible de créer le token de messagerie");
        }
    }
}
