package org.example.jobservice.config;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // routes spécifiques en PREMIER (avant les patterns génériques /{id})
                        .requestMatchers(HttpMethod.GET, "/api/offers/my").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/offers/{id}/applications").hasRole("CLIENT")
                        // routes publiques
                        .requestMatchers(HttpMethod.GET, "/api/offers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/offers/{id}").permitAll()
                        // offres : CLIENT uniquement
                        .requestMatchers(HttpMethod.POST, "/api/offers").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/offers/{id}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/offers/{id}/close").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/offers/{id}").hasRole("CLIENT")
                        // candidatures : FREELANCER uniquement
                        .requestMatchers(HttpMethod.POST, "/api/applications").hasRole("FREELANCER")
                        .requestMatchers(HttpMethod.GET, "/api/applications/my").hasRole("FREELANCER")
                        .requestMatchers(HttpMethod.GET, "/api/applications/{id}").hasRole("FREELANCER")
                        .requestMatchers(HttpMethod.PUT, "/api/applications/{id}/complete").hasRole("FREELANCER")
                        .requestMatchers(HttpMethod.DELETE, "/api/applications/{id}").hasRole("FREELANCER")
                        // accept/reject candidature : CLIENT uniquement
                        .requestMatchers(HttpMethod.PUT, "/api/applications/{id}/accept").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/applications/{id}/reject").hasRole("CLIENT")
                        // favoris : FREELANCER uniquement
                        .requestMatchers("/api/favorites/**").hasRole("FREELANCER")
                        // notifications : tout utilisateur connecté
                        .requestMatchers("/api/notifications/**").authenticated()
                        // tout le reste nécessite d'être connecté
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}