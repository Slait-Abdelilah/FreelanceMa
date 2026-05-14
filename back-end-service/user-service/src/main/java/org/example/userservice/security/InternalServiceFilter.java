package org.example.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalServiceFilter extends OncePerRequestFilter {

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    @Value("${app.internal.secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/internal/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedSecret = request.getHeader(INTERNAL_SECRET_HEADER);
        if (providedSecret == null || !providedSecret.equals(internalSecret)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"Acces interne refuse\"}");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "internal-service",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_INTERNAL"))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
