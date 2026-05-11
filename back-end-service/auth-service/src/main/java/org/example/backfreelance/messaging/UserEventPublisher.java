package org.example.backfreelance.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // Publie un événement "user.registered" vers RabbitMQ
    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("[RabbitMQ] Publication → {} : userId={}", RabbitMQConfig.ROUTING_REGISTERED, event.getUserId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.ROUTING_REGISTERED,
                event
        );
    }

    // Publie un événement "user.deleted" vers RabbitMQ
    public void publishUserDeleted(UserDeletedEvent event) {
        log.info("[RabbitMQ] Publication → {} : userId={}", RabbitMQConfig.ROUTING_DELETED, event.getUserId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.ROUTING_DELETED,
                event
        );
    }
}
