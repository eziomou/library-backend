package pl.zmudzin.library.core.application.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.account.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final Account account = new Account(AccountId.of(""), "Foo", "Bar", new Profile("Baz", "Qux"));

    @Test
    void updatePassword_existingAccount_updatesPassword() {
        when(accountRepository.find(account.getId())).thenReturn(Optional.of(account));
        when(passwordEncoder.encode("Foo")).thenReturn("Bar");

        accountService.updatePassword(account.getId().toString(), "Foo");

        verify(passwordEncoder, times(1)).encode("Foo");
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void updatePassword_notExistingAccount_throwsException() {
        when(accountRepository.find(account.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.updatePassword(account.getId().toString(), "Secret"));
    }

    @Test
    void updateProfile_existingAccount_updatesProfile() {
        when(accountRepository.find(account.getId())).thenReturn(Optional.of(account));

        accountService.updateProfile(account.getId().toString(), "Foo", "Bar");

        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void updateProfile_notExistingAccount_throwsException() {
        when(accountRepository.find(account.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.updateProfile(account.getId().toString(), "Foo", "Bar"));
    }
}