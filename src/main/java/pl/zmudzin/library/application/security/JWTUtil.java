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

    private static final Long EXPIRATION_TIME = 864000000L;

    public static final String AUTHORITY_PREFIX = Roles.PREFIX;
    public static final String AUTHORITIES_CLAIM = "roles";

    public static String getToken(Authentication authentication, String secret) {
        return JWT.create()
                .withSubject(JWTUtil.createSubject(authentication))
                .withArrayClaim(JWTUtil.AUTHORITIES_CLAIM, JWTUtil.mapAuthorities(authentication.getAuthorities()))
                .withExpiresAt(JWTUtil.getExpirationDate())
                .sign(Algorithm.HMAC256(secret));
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

    private static Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public static Authentication getAuthentication(String token, String secret) {
        DecodedJWT decodedToken = JWT.require(Algorithm.HMAC256(secret)).build()
                .verify(token);

        return new UsernamePasswordAuthenticationToken(
                JWTUtil.getPrincipal(decodedToken),
                null,
                JWTUtil.getAuthorities(decodedToken)
        );
    }

    public static Object getPrincipal(DecodedJWT decodedToken) {
        return User.builder()
                .username(decodedToken.getSubject())
                .password("")
                .authorities(getAuthorities(decodedToken))
                .build();
    }

    public static Collection<GrantedAuthority> getAuthorities(DecodedJWT decodedToken) {
        if (!decodedToken.getClaim(AUTHORITIES_CLAIM).isNull()) {
            return decodedToken.getClaim(AUTHORITIES_CLAIM).asList(String.class).stream()
                    .map(JWTUtil::mapAuthority)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static SimpleGrantedAuthority mapAuthority(String authority) {
        return new SimpleGrantedAuthority(AUTHORITY_PREFIX + authority.toUpperCase());
    }
}
