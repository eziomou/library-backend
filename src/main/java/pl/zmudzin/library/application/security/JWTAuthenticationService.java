package pl.zmudzin.library.application.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class JWTAuthenticationService extends AbstractAuthenticationService {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(createAuthentication(username, password));
        return JWTUtil.createToken(authentication);
    }

    private Authentication createAuthentication(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
