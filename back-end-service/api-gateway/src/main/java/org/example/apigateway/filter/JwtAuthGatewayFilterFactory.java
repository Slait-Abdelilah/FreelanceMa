package org.example.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    @Value("${app.jwt.secret}")
    private String secret;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/api/auth/verify",
            "/api/auth/verify-account",
            "/api/auth/verify-reset-code",
            "/api/auth/resend-code",
            "/api/auth/forgot-password",
            "/api/auth/reset-password"
            // /api/auth/change-password et /api/auth/account (DELETE) sont protégés
    );

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("[Gateway] Requête sans token Bearer : {}", path);
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            Claims claims;
            try {
                claims = extractClaims(token);
            } catch (JwtException e) {
                log.warn("[Gateway] Token JWT invalide : {}", e.getMessage());
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            if (claims.getExpiration().before(new Date())) {
                log.warn("[Gateway] Token JWT expiré");
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String userId = claims.get("userId") != null ? claims.get("userId").toString() : "";
            String role   = claims.get("role", String.class);
            String email  = claims.getSubject();

            log.debug("[Gateway] Token valide — email={} role={} userId={}", email, role, userId);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .header("X-User-Email", email)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith)
                || path.matches("/api/offers/\\d+")
                || path.equals("/api/offers")
                || path.matches("/api/profile/[^/]+/public")
                || path.matches("/api/portfolio/public/.*");
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        return response.writeWith(Mono.just(
                response.bufferFactory().wrap(
                        ("{\"error\":\"" + status.getReasonPhrase() + "\"}").getBytes(StandardCharsets.UTF_8)
                )
        ));
    }

    public static class Config {}
}
