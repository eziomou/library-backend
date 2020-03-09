package pl.zmudzin.library.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Piotr Żmudzin
 */
class JWTUtil {
    
    private static final String SECRET = "11DBA47AD0E6DFE15DB291A8A4E6E9CEF6CB9C791D3D7A4301DEE4A388DA9C99";
    private static final Long EXPIRATION_TIME = 864000000L;

    private static final String AUTHORITY_PREFIX = "ROLE_";
    private static final String AUTHORITIES_CLAIM = "roles";

    static DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build()
                .verify(token);
    }

    static String createToken(Authentication authentication) {
        return JWT.create()
                .withSubject(createSubject(authentication))
                .withArrayClaim(AUTHORITIES_CLAIM, mapAuthorities(authentication.getAuthorities()))
                .withExpiresAt(calculateExpirationDate())
                .sign(Algorithm.HMAC256(SECRET));
    }

    private static String createSubject(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return authentication.getPrincipal().toString();
    }

    private static String[] mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(JWTUtil::mapAuthority).toArray(String[]::new);
    }

    private static <A extends GrantedAuthority> String mapAuthority(A authority) {
        return authority.getAuthority().replace(AUTHORITY_PREFIX, "").toLowerCase();
    }

    private static Date calculateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    static Authentication createAuthentication(DecodedJWT decodedJWT) {
        return new UsernamePasswordAuthenticationToken(createPrincipal(decodedJWT), null,
                createAuthorities(decodedJWT));
    }

    private static Object createPrincipal(DecodedJWT decodedJWT) {
        return User.builder()
                .username(decodedJWT.getSubject())
                .password("")
                .authorities(createAuthorities(decodedJWT))
                .build();
    }

    private static Collection<GrantedAuthority> createAuthorities(DecodedJWT decodedJWT) {
        if (!decodedJWT.getClaim(AUTHORITIES_CLAIM).isNull()) {
            return decodedJWT.getClaim(AUTHORITIES_CLAIM).asList(String.class).stream()
                    .map(JWTUtil::mapAuthority)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static SimpleGrantedAuthority mapAuthority(String authority) {
        return new SimpleGrantedAuthority(AUTHORITY_PREFIX + authority.toUpperCase());
    }
}
