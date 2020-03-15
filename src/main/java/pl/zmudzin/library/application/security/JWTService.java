package pl.zmudzin.library.application.security;

import org.springframework.security.core.Authentication;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface JWTService {

    String getToken(Authentication authentication);

    Authentication getAuthentication(String token);
}
