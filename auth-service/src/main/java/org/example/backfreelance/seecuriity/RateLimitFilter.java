package org.example.backfreelance.seecuriity;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60_000L;

    private static final List<String> RATE_LIMITED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/forgot-password",
            "/api/auth/verify-account",
            "/api/auth/verify-reset-code",
            "/api/auth/resend-code",
            "/api/auth/register"
    );

    // Only trust X-Forwarded-For from these IPs (reverse proxy / load balancer addresses)
    // Configure via app.security.trusted-proxies in application.yml
    @Value("${app.security.trusted-proxies:}")
    private String trustedProxiesConfig;

    private Set<String> trustedProxies;

    // IP -> sliding window of request timestamps
    private final Map<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        trustedProxies = Arrays.stream(trustedProxiesConfig.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (RATE_LIMITED_PATHS.stream().noneMatch(path::endsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = resolveClientIp(request);
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_MS;

        Deque<Long> timestamps = requestLog.computeIfAbsent(clientIp, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= MAX_REQUESTS) {
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"Trop de tentatives. Veuillez réessayer dans 1 minute.\"}");
                return;
            }
            timestamps.addLast(now);
        }

        filterChain.doFilter(request, response);
    }

    // Prevent unbounded memory growth — runs every 5 minutes
    @Scheduled(fixedRate = 300_000)
    public void cleanupStaleEntries() {
        long windowStart = System.currentTimeMillis() - WINDOW_MS;
        requestLog.entrySet().removeIf(entry -> {
            synchronized (entry.getValue()) {
                while (!entry.getValue().isEmpty() && entry.getValue().peekFirst() < windowStart) {
                    entry.getValue().pollFirst();
                }
                return entry.getValue().isEmpty();
            }
        });
    }

    private String resolveClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        // Only trust X-Forwarded-For when the direct caller is a known trusted proxy
        if (trustedProxies.contains(remoteAddr)) {
            String xff = request.getHeader("X-Forwarded-For");
            if (xff != null && !xff.isBlank()) {
                return xff.split(",")[0].trim();
            }
        }
        return remoteAddr;
    }
}