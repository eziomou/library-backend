package pl.zmudzin.library.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthorizationFilter.class);

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer";

    private final AuthenticationManager authenticationManager;

    public TokenAuthorizationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HEADER);

        if (!isValid(header)) {
            chain.doFilter(request, response);
            return;
        }
        String token = extractToken(header);
        try {
            logger.debug("Trying to authenticate using token: {}", token);
            Authentication authentication = authenticationManager.authenticate(new JwtAuthenticationToken(token));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Failed to authenticate using token: {}", token);
        }
        chain.doFilter(request, response);
    }

    private boolean isValid(String header) {
        return header != null && header.startsWith(PREFIX);
    }

    private String extractToken(String header) {
        return header.substring(PREFIX.length()).trim();
    }
}
