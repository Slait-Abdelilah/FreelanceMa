package org.example.userservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.entity.UserProfile;
import org.example.userservice.entity.Wallet;
import org.example.userservice.repository.PortfolioRepository;
import org.example.userservice.repository.UserProfileRepository;
import org.example.userservice.repository.WalletRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserProfileRepository userProfileRepository;
    private final WalletRepository      walletRepository;
    private final PortfolioRepository   portfolioRepository;

    /**
     * Déclenché quand un utilisateur vérifie son email et active son compte.
     * On crée automatiquement son profil et son wallet.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_REGISTERED)
    @Transactional
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("[RabbitMQ] Reçu user.registered pour userId={}", event.getUserId());

        // Créer le profil (si pas déjà existant)
        if (!userProfileRepository.existsById(event.getUserId())) {
            UserProfile profile = UserProfile.builder()
                    .userId(event.getUserId())
                    .email(event.getEmail())
                    .firstName(event.getFirstName())
                    .lastName(event.getLastName())
                    .role(event.getRole())
                    .build();
            userProfileRepository.save(profile);
            log.info("[RabbitMQ] Profil créé pour userId={}", event.getUserId());
        }

        // Créer le wallet (si pas déjà existant)
        if (!walletRepository.existsByUserId(event.getUserId())) {
            Wallet wallet = Wallet.builder()
                    .userId(event.getUserId())
                    .balance(BigDecimal.ZERO)
                    .pendingBalance(BigDecimal.ZERO)
                    .totalEarned(BigDecimal.ZERO)
                    .build();
            walletRepository.save(wallet);
            log.info("[RabbitMQ] Wallet créé pour userId={}", event.getUserId());
        }
    }

    /**
     * Déclenché quand un compte est supprimé.
     * On nettoie toutes les données du user dans ce service.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_DELETED)
    @Transactional
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("[RabbitMQ] Reçu user.deleted pour userId={}", event.getUserId());

        portfolioRepository.deleteByFreelancerId(event.getUserId());
        walletRepository.deleteByUserId(event.getUserId());
        userProfileRepository.deleteByUserId(event.getUserId());

        log.info("[RabbitMQ] Données supprimées pour userId={}", event.getUserId());
    }
}
