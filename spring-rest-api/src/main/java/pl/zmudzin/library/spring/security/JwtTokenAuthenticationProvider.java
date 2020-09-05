package pl.zmudzin.library.spring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DecodedJWT token = decode(cast(authentication).getToken());
        return new JwtAuthenticationToken(token.getToken(), token.getSubject(), null, extractAuthorities(token));
    }

    private JwtAuthenticationToken cast(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return (JwtAuthenticationToken) authentication;
        }
        throw new IllegalStateException();
    }

    private DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    private Collection<GrantedAuthority> extractAuthorities(DecodedJWT token) {
        Claim roles = token.getClaim("roles");
        if (roles.isNull()) {
            return Collections.emptySet();
        }
        return roles.asList(String.class).stream().map(this::parseAuthority).collect(Collectors.toSet());
    }

    private SimpleGrantedAuthority parseAuthority(String authority) {
        return new SimpleGrantedAuthority("ROLE_" + authority.toUpperCase());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
