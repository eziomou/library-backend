package pl.zmudzin.library.application.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.library.domain.common.Resource;
import pl.zmudzin.library.domain.member.Member;

/**
 * @author Piotr Żmudzin
 */
public abstract class AbstractAuthenticationService implements AuthenticationService {

    @Override
    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isAuthenticated(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return authentication;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated(SecurityContextHolder.getContext().getAuthentication());
    }

    private static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public UserDetails getUserDetails() {
        return (UserDetails) getAuthentication().getPrincipal();
    }

    @Override
    public String getUsername() {
        return getUserDetails().getUsername();
    }

    @Override
    public boolean hasUsername(String username) {
        return getUsername().equals(username);
    }

    @Override
    public boolean hasAuthority(String authority) {
        return getUserDetails().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    @Override
    public boolean isResourceOwner(Resource<Member> resource) {
        return getUsername().equals(resource.getOwner().getAccount().getUsername());
    }
}
