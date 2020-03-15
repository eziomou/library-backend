package pl.zmudzin.library.application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class UserDetailsBuilder {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private UserDetailsBuilder() {
    }

    public UserDetailsBuilder username(String username) {
        this.username = Objects.requireNonNull(username);
        return this;
    }

    public UserDetailsBuilder password(String passwordHash) {
        this.password = Objects.requireNonNull(passwordHash);
        return this;
    }

    public UserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = Objects.requireNonNull(authorities);
        return this;
    }

    public UserDetails build() {
        return new UserDetailsImpl(username, password, authorities);
    }

    public static UserDetailsBuilder builder() {
        return new UserDetailsBuilder();
    }
}
