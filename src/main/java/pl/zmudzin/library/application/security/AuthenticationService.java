package pl.zmudzin.library.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import pl.zmudzin.ddd.annotations.application.ApplicationService;
import pl.zmudzin.library.domain.common.Resource;
import pl.zmudzin.library.domain.member.Member;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface AuthenticationService {

    Object authenticate(String username, String password);

    Authentication getAuthentication();

    boolean isAuthenticated();

    UserDetails getUserDetails();

    String getUsername();

    boolean hasUsername(String username);

    boolean hasAuthority(String authority);

    boolean isResourceOwner(Resource<Member> resource);
}
