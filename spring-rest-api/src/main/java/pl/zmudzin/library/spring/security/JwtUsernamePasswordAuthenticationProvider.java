package pl.zmudzin.library.spring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.librarian.LibrarianRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtUsernamePasswordAuthenticationProvider(AccountRepository accountRepository,
                                                     MemberRepository memberRepository,
                                                     LibrarianRepository librarianRepository,
                                                     @Lazy PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.librarianRepository = librarianRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + username));

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (memberRepository.existsByAccountId(account.getId())) {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER));
        }

        if (librarianRepository.existsByAccountId(account.getId())) {
            authorities.add(new SimpleGrantedAuthority(Role.LIBRARIAN));
        }

        if (passwordEncoder.matches(password, account.getPassword())) {
            return new JwtAuthenticationToken(generateToken(username, authorities), username, null, authorities);
        }
        throw new BadCredentialsException("Invalid credentials for account: " + username);
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return JWT.create()
                .withSubject(username)
                .withArrayClaim("roles", toStringArray(authorities))
                .withExpiresAt(getExpirationDate())
                .sign(Algorithm.HMAC256(secret));
    }

    private String[] toStringArray(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(this::toString).toArray(String[]::new);
    }

    private String toString(GrantedAuthority authority) {
        return authority.getAuthority().replace("ROLE_", "").toLowerCase();
    }

    private static Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + 864000000L);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
