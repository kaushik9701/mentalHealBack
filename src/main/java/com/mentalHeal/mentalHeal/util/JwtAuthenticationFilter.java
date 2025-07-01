package com.mentalHeal.mentalHeal.util;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        logger.info("🔍 Incoming request: " + request.getMethod() + " " + path);

        try {
            String authHeader = request.getHeader(AUTH_HEADER);
            logger.info("🔐 Authorization header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                logger.warn("🚫 No Bearer token found, skipping authentication");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(BEARER_PREFIX.length());
            logger.info("🧪 Extracted JWT: " + token);

            String email = jwtUtil.validateTokenAndGetEmail(token);
            logger.info("✅ Email from token: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                logger.info("👤 Loaded user: " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("🔓 User authenticated and added to context");
            }
        } catch (UsernameNotFoundException e) {
            logger.error("❗ User not found while processing JWT", e);
        } catch (Exception e) {
            logger.error("❗ Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}