package pl.zmudzin.library.application.member;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountFactory;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class MemberServiceImpl implements MemberService {

    private AccountFactory accountFactory;
    private AccountRepository accountRepository;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    public MemberServiceImpl(AccountFactory accountFactory, AccountRepository accountRepository,
                             MemberRepository memberRepository, @Lazy PasswordEncoder passwordEncoder,
                             AuthenticationService authenticationService) {
        this.accountFactory = accountFactory;
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MemberData createMember(MemberCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Account account = accountFactory.createAccount(
                request.getUsername(),
                encodedPassword,
                request.getFirstName(),
                request.getLastName()
        );
        account = accountRepository.save(account);

        Member member = new Member(account);
        member = memberRepository.save(member);

        return map(member);
    }

    @Override
    public MemberData getMemberByUsername(String username) {
        Member member = getMemberEntityByUsername(username);
        return map(member);
    }

    @Secured(Roles.LIBRARIAN)
    @Override
    public Page<MemberData> getAllMembers(MemberSearchRequest request, Pageable pageable) {
        Specification<Member> specification = (r, cq, cb) -> MemberPredicateBuilder.builder(r, cb)
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFullName())
                .build();

        return memberRepository.findAll(specification, pageable).map(this::map);
    }

    private Member getMemberEntityByUsername(String username) {
        Member member = memberRepository.findByAccountUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!authenticationService.isResourceOwner(member) &&
            !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return member;
    }

    private MemberData map(Member member) {
        MemberData data = new MemberData();
        data.setUsername(member.getAccount().getUsername());
        data.setFirstName(member.getAccount().getProfile().getFirstName());
        data.setLastName(member.getAccount().getProfile().getLastName());
        return data;
    }
}
