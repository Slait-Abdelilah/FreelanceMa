package org.example.backfreelance.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ── Noms des échanges, queues et routing keys ──────────────────────────
    public static final String USER_EXCHANGE       = "user.exchange";
    public static final String ROUTING_REGISTERED  = "user.registered";
    public static final String ROUTING_DELETED     = "user.deleted";
    public static final String QUEUE_REGISTERED    = "user.registered.queue";
    public static final String QUEUE_DELETED       = "user.deleted.queue";

    // ── Exchange : un seul point d'entrée pour tous les événements user ────
    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE);
    }

    // ── Queues durables (survivent au redémarrage de RabbitMQ) ────────────
    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(QUEUE_REGISTERED, true);
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(QUEUE_DELETED, true);
    }

    // ── Liaisons exchange → queue via routing key ─────────────────────────
    @Bean
    public Binding bindRegistered(Queue userRegisteredQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(userRegisteredQueue).to(userExchange).with(ROUTING_REGISTERED);
    }

    @Bean
    public Binding bindDeleted(Queue userDeletedQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with(ROUTING_DELETED);
    }

    // ── Convertisseur JSON : les messages sont sérialisés en JSON ─────────
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
