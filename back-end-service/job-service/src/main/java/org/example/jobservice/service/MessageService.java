package org.example.jobservice.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.dto.ChatMessageDTO;
import org.example.jobservice.dto.ConversationTokenDTO;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.ChatMessage;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.exception.ConflictException;
import org.example.jobservice.exception.ForbiddenException;
import org.example.jobservice.exception.ResourceNotFoundException;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.ChatMessageRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationRepository applicationRepository;
    private final OfferRepository       offerRepository;

    private static final Set<ApplicationStatus> ALLOWED_STATUSES = Set.of(
            ApplicationStatus.ACCEPTED,
            ApplicationStatus.AWAITING_VALIDATION,
            ApplicationStatus.COMPLETED
    );

    public void checkAccess(String conversationId, Long userId, String role) {
        Long applicationId = parseApplicationId(conversationId);
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));
        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        boolean isClient     = "CLIENT".equals(role)     && offer.getClientId().equals(userId);
        boolean isFreelancer = "FREELANCER".equals(role) && app.getFreelancerId().equals(userId);
        if (!isClient && !isFreelancer) {
            log.warn("[MessageService] checkAccess FAILED — conv={} userId={} role={} | app.freelancerId={} offer.clientId={}",
                    conversationId, userId, role, app.getFreelancerId(), offer.getClientId());
            throw new ForbiddenException("Vous n'etes pas participant de cette conversation");
        }
        if (!ALLOWED_STATUSES.contains(app.getStatus())) {
            throw new ConflictException("La messagerie est disponible uniquement pour les missions acceptees");
        }
    }

    public ChatMessageDTO saveMessage(String conversationId, Long senderId, String senderRole, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setConversationId(conversationId);
        msg.setSenderId(senderId);
        msg.setSenderRole(senderRole);
        msg.setContent(content);
        msg.setCreatedAt(Instant.now());
        return toDTO(chatMessageRepository.save(msg));
    }

    @Transactional
    public List<ChatMessageDTO> getHistory(String conversationId, Long userId, String role) {
        checkAccess(conversationId, userId, role);
        List<ChatMessageDTO> history = chatMessageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream().map(this::toDTO).toList();
        chatMessageRepository.markConversationRead(conversationId, userId);
        return history;
    }

    public long getUnreadCount(Long userId, String role) {
        List<String> convIds;
        if ("FREELANCER".equals(role)) {
            convIds = applicationRepository.findByFreelancerIdOrderByCreatedAtDesc(userId)
                    .stream()
                    .filter(a -> ALLOWED_STATUSES.contains(a.getStatus()))
                    .map(a -> "conv_" + a.getId())
                    .toList();
        } else {
            List<Long> offerIds = offerRepository.findByClientIdOrderByCreatedAtDesc(userId)
                    .stream().map(org.example.jobservice.entity.Offer::getId).toList();
            if (offerIds.isEmpty()) return 0;
            convIds = applicationRepository.findByOfferIdIn(offerIds)
                    .stream()
                    .filter(a -> ALLOWED_STATUSES.contains(a.getStatus()))
                    .map(a -> "conv_" + a.getId())
                    .toList();
        }
        if (convIds.isEmpty()) return 0;
        return chatMessageRepository.countUnread(convIds, userId);
    }

    public ConversationTokenDTO getConversationToken(Long applicationId, Long userId, String role) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));
        Offer offer = offerRepository.findById(app.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));

        String convId = "conv_" + applicationId;

        boolean isClient     = "CLIENT".equals(role)     && offer.getClientId().equals(userId);
        boolean isFreelancer = "FREELANCER".equals(role) && app.getFreelancerId().equals(userId);
        if (!isClient && !isFreelancer) {
            log.warn("[MessageService] Token denied — conv={} userId={} role={} | app.freelancerId={} offer.clientId={}",
                    convId, userId, role, app.getFreelancerId(), offer.getClientId());
            throw new ForbiddenException("Vous n'êtes pas participant de cette conversation");
        }
        if (!ALLOWED_STATUSES.contains(app.getStatus())) {
            throw new ConflictException("La messagerie est disponible uniquement pour les missions acceptées");
        }

        Long   otherUserId;
        String otherUserRole;
        if ("CLIENT".equals(role)) {
            otherUserId   = app.getFreelancerId();
            otherUserRole = "FREELANCER";
        } else {
            otherUserId   = offer.getClientId();
            otherUserRole = "CLIENT";
        }

        if (FirebaseApp.getApps().isEmpty()) {
            throw new ConflictException("Service Firebase non disponible — vérifiez firebase-service-account.json");
        }
        String firebaseToken;
        try {
            firebaseToken = FirebaseAuth.getInstance().createCustomToken(String.valueOf(userId));
        } catch (Exception e) {
            log.error("[MessageService] Firebase token generation failed: {}", e.getMessage());
            throw new ConflictException("Impossible de générer le token Firebase : " + e.getMessage());
        }

        return ConversationTokenDTO.builder()
                .firebaseToken(firebaseToken)
                .conversationId(convId)
                .otherUserId(otherUserId)
                .otherUserRole(otherUserRole)
                .offerTitle(offer.getTitle())
                .build();
    }

    public boolean isFirebaseAvailable() {
        if (FirebaseApp.getApps().isEmpty()) return false;
        try {
            FirebaseAuth.getInstance().createCustomToken("health-check");
            return true;
        } catch (Exception e) {
            log.warn("[MessageService] Firebase health check failed: {}", e.getMessage());
            return false;
        }
    }

    private Long parseApplicationId(String conversationId) {
        try {
            return Long.parseLong(conversationId.replace("conv_", ""));
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Identifiant de conversation invalide: " + conversationId);
        }
    }

    private ChatMessageDTO toDTO(ChatMessage msg) {
        return ChatMessageDTO.builder()
                .id(msg.getId())
                .conversationId(msg.getConversationId())
                .senderId(String.valueOf(msg.getSenderId()))
                .senderRole(msg.getSenderRole())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}
