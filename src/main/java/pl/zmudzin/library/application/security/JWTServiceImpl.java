package pl.zmudzin.library.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;

/**
 * Basic implementation of {@code JWTService}.
 *
 * <p>The password hash and secret key are used to sign the token, which ensures
 * that the token will no longer be valid when the password is changed.</p>
 *
 * @see JWTService
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class JWTServiceImpl implements JWTService {

    @Value("${app.jwt.secret}")
    private String secret;

    private UserDetailsService userDetailsService;

    public JWTServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String getToken(Authentication authentication) {
        String password = ((UserDetails) authentication.getPrincipal()).getPassword();
        return JWTUtil.getToken(authentication, combine(password, secret));
    }

    @Override
    public Authentication getAuthentication(String token) {
        String password = getPassword(token);
        return JWTUtil.getAuthentication(token, combine(password, secret));
    }

    private String getPassword(String token) {
        DecodedJWT decodedToken = JWT.decode(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedToken.getSubject());
        return userDetails.getPassword();
    }

    private static String combine(String password, String secret) {
        return password + secret;
    }
}
