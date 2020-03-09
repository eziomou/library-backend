package pl.zmudzin.library.application.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountFactory;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private AccountFactory accountFactory;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void createMember_validArguments_createsMember() {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();
        String encodedPassword = "encodedPassword";
        Account account = AccountTestUtil.mockAccount();
        Member member = MemberTestUtil.mockMember(1L, account);

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(accountFactory.createAccount(request.getUsername(), encodedPassword, request.getFirstName(),
                request.getLastName())).thenReturn(account);

        when(accountRepository.save(account)).thenReturn(account);
        when(memberRepository.save(any())).thenReturn(member);

        MemberData data = memberService.createMember(request);

        verify(accountRepository, times(1)).save(account);
        verify(memberRepository, times(1)).save(any());

        assertMemberEquals(member, data);
    }

    @Test
    void getMemberByUsername_notExistingMember_throwsException() {
        when(memberRepository.findByAccountUsername(AccountTestUtil.USERNAME)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> memberService.getMemberByUsername(AccountTestUtil.USERNAME));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getMemberByUsername_notAuthorized_throwsException() {
        Member member = MemberTestUtil.mockMember();

        when(memberRepository.findByAccountUsername(AccountTestUtil.USERNAME)).thenReturn(Optional.of(member));
        when(authenticationService.isResourceOwner(member)).thenReturn(false);
        when(authenticationService.hasAuthority(Roles.LIBRARIAN)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> memberService.getMemberByUsername(AccountTestUtil.USERNAME));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void getMemberByUsername_asOwner_returnsMember() {
        Member member = MemberTestUtil.mockMember();

        when(memberRepository.findByAccountUsername(AccountTestUtil.USERNAME)).thenReturn(Optional.of(member));
        when(authenticationService.isResourceOwner(member)).thenReturn(true);

        MemberData data = memberService.getMemberByUsername(AccountTestUtil.USERNAME);

        assertMemberEquals(member, data);
    }

    @Test
    void getMemberByUsername_asLibrarian_returnsMember() {
        Member member = MemberTestUtil.mockMember();

        when(memberRepository.findByAccountUsername(AccountTestUtil.USERNAME)).thenReturn(Optional.of(member));
        when(authenticationService.isResourceOwner(member)).thenReturn(true);

        MemberData data = memberService.getMemberByUsername(AccountTestUtil.USERNAME);

        assertMemberEquals(member, data);
    }

    private static void assertMemberEquals(Member member, MemberData data) {
        assertAll(
                () -> assertEquals(member.getAccount().getUsername(), data.getUsername()),
                () -> assertEquals(member.getAccount().getProfile().getFirstName(), data.getFirstName()),
                () -> assertEquals(member.getAccount().getProfile().getLastName(), data.getLastName())
        );
    }
}