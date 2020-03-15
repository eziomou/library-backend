package pl.zmudzin.library.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Piotr Żmudzin
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    private JWTService jwtService;

    public JWTAuthorizationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(TOKEN_HEADER);

        if (!isValid(header)) {
            chain.doFilter(request, response);
            return;
        }
        String token = extractToken(header);

        Authentication authentication = null;
        try {
            authentication = jwtService.getAuthentication(token);
        } catch (Exception ignored) {
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private boolean isValid(String header) {
        return header != null && header.startsWith(TOKEN_PREFIX);
    }

    private String extractToken(String header) {
        return header.substring(TOKEN_PREFIX.length()).trim();
    }
}
