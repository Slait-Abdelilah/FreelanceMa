package org.example.jobservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalFeignConfig {

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    @Bean
    public RequestInterceptor internalServiceRequestInterceptor(
            @Value("${app.internal.secret}") String internalSecret) {
        return template -> {
            if (template.path().startsWith("/internal/")) {
                template.header(INTERNAL_SECRET_HEADER, internalSecret);
            }
        };
    }
}
