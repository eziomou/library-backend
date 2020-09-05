package pl.zmudzin.library.spring.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.librarian.LibrarianRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;

@Component
public class SpringAuthorizationService implements AuthorizationService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;

    public SpringAuthorizationService(AccountRepository accountRepository, MemberRepository memberRepository, LibrarianRepository librarianRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.librarianRepository = librarianRepository;
    }

    @Override
    public String getAccountId() {
        return accountRepository.findByUsername(getAuthentication().getName())
                .map(a -> a.getId().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    @Override
    public String getMemberId() {
        return memberRepository.findByUsername(getAuthentication().getName())
                .map(m -> m.getId().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    @Override
    public String getLibrarianId() {
        return librarianRepository.findByUsername(getAuthentication().getName())
                .map(l -> l.getId().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return authentication;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }
}
