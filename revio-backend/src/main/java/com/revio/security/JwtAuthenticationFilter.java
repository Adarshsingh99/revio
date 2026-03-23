package com.revio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT Authentication Filter.
 * 
 * This filter intercepts incoming requests and:
 * 1. Extracts JWT token from Authorization header
 * 2. Validates the token
 * 3. Sets the authenticated user in Spring Security context
 * 4. Allows the request to proceed if valid, denies if invalid
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Filter method that processes each HTTP request.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract JWT token from Authorization header
            String bearerToken = request.getHeader("Authorization");
            
            if (bearerToken != null) {
                String token = jwtTokenProvider.extractTokenFromBearer(bearerToken);

                // Validate the token
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    String userId = jwtTokenProvider.getUserIdFromToken(token);
                    String email = jwtTokenProvider.getEmailFromToken(token);

                    // Create authentication token and set it in SecurityContext
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                    userId, 
                                    null, 
                                    new ArrayList<>()); // authorities

                    // Store userId and email in the authentication token for later use
                    authentication.setDetails(email);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Set Spring Security authentication for user: {}", userId);
                } else if (token != null) {
                    log.warn("Invalid JWT token provided");
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Determine if this filter should be applied to the given request.
     * Skip the filter for public endpoints like auth.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip JWT filter for authentication endpoints
        return path.contains("/auth/");
    }
}
