package pl.zmudzin.library.application.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.librarian.LibrarianRepository;
import pl.zmudzin.library.domain.member.MemberRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Piotr Żmudzin
 */
@Primary
@ApplicationServiceImpl
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;
    private MemberRepository memberRepository;
    private LibrarianRepository librarianRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository, MemberRepository memberRepository,
                                  LibrarianRepository librarianRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.librarianRepository = librarianRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (memberRepository.existsByAccountId(account.getId())) {
            authorities.add(new SimpleGrantedAuthority(Roles.MEMBER));
        }
        if (librarianRepository.existsByAccountId(account.getId())) {
            authorities.add(new SimpleGrantedAuthority(Roles.LIBRARIAN));
        }
        return map(account, authorities);
    }

    private UserDetails map(Account account, Collection<GrantedAuthority> authorities) {
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}
