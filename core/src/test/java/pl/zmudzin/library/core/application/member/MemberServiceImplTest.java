package pl.zmudzin.library.core.application.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.domain.account.*;
import pl.zmudzin.library.core.domain.member.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

    @Mock
    private AccountFactory accountFactory;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberReadonlyRepository memberReadonlyRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void registerMember_existingAccount_throwsException() {
        RegisterMemberCommand command = RegisterMemberCommand.builder()
                .username("Foo").password("Bar")
                .firstName("Baz").lastName("Qux")
                .build();

        when(accountFactory.createAccount(command.getUsername(), command.getPassword(),
                command.getFirstName(), command.getLastName())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(command));

        verify(accountRepository, times(0)).save(any());
        verify(memberRepository, times(0)).save(any());
    }

    @Test
    void registerMember_notExistingAccount_registersMember() {
        RegisterMemberCommand command = RegisterMemberCommand.builder()
                .username("Foo").password("Bar")
                .firstName("Baz").lastName("Qux")
                .build();

        Account account = new Account(AccountId.of(""), command.getUsername(), command.getPassword(),
                new Profile(command.getFirstName(), command.getLastName()));

        when(accountFactory.createAccount(command.getUsername(), command.getPassword(),
                command.getFirstName(), command.getLastName())).thenReturn(account);

        memberService.registerMember(command);

        verify(accountRepository, times(1)).save(any());
        verify(memberRepository, times(1)).save(any());
    }
}

