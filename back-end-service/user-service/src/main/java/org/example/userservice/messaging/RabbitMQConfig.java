package org.example.userservice.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_REGISTERED = "user.registered.queue";
    public static final String QUEUE_DELETED    = "user.deleted.queue";

    // Déclaration explicite des queues : Spring AMQP les crée dans RabbitMQ si elles n'existent pas
    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(QUEUE_REGISTERED, true);
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(QUEUE_DELETED, true);
    }

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
