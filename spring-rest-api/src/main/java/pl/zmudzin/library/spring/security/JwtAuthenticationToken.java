package pl.zmudzin.library.spring.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;
    private final String token;

    public JwtAuthenticationToken(String token) {
        this(token, null, null);
        super.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String token, Object principal, Object credentials) {
        this(token, principal, credentials, null);
    }

    public JwtAuthenticationToken(String token, Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.token = token;
        super.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public String getToken() {
        return token;
    }
}
