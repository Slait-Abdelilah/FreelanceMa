package org.example.jobservice.config;

import feign.RequestInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class InternalFeignConfig {

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";
    private static final String DEFAULT_SECRET = "dev-internal-secret-change-me";

    @Value("${app.internal.secret}")
    private String internalSecret;

    @PostConstruct
    public void validateInternalSecret() {
        if (DEFAULT_SECRET.equals(internalSecret)) {
            log.warn("⚠️  AVERTISSEMENT SÉCURITÉ : INTERNAL_API_SECRET utilise la valeur par défaut. " +
                     "Définissez la variable d'environnement INTERNAL_API_SECRET avant de déployer en production !");
        }
    }

    @Bean
    public RequestInterceptor internalServiceRequestInterceptor() {
        return template -> {
            if (template.path().startsWith("/internal/")) {
                template.header(INTERNAL_SECRET_HEADER, internalSecret);
            }
        };
    }
}
