package pl.zmudzin.library.application.account;

import org.junit.jupiter.api.BeforeAll;
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
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.util.AccountTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static Account account;

    @BeforeAll
    static void beforeAll() {
        account = AccountTestUtil.mockAccount();
    }

    @Test
    void getAccountByUsername_nonExistingAccount_throwsException() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> accountService.getAccountByUsername(account.getUsername()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getAccountByUsername_notAuthorized_throwsException() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(authenticationService.hasUsername(account.getUsername())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> accountService.getAccountByUsername(account.getUsername()));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void getAccountByUsername_asOwner_returnsAccountData() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(authenticationService.hasUsername(account.getUsername())).thenReturn(true);

        AccountData data = accountService.getAccountByUsername(account.getUsername());

        assertAccountEquals(account, data);
    }

    @Test
    void getAccountByUsername_asLibrarian_returnsAccountData() {
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(authenticationService.hasUsername(account.getUsername())).thenReturn(false);
        when(authenticationService.hasAuthority(Roles.LIBRARIAN)).thenReturn(true);

        AccountData data = accountService.getAccountByUsername(account.getUsername());

        assertAccountEquals(account, data);
    }

    @Test
    void updateAccountByUsername_nonExistingAccount_throwsException() {
        AccountUpdateRequest request = new AccountUpdateRequest();

        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> accountService.updateAccountByUsername(account.getUsername(), request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateAccountByUsername_notAuthorized_throwsException() {
        AccountUpdateRequest request = new AccountUpdateRequest();

        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(authenticationService.hasUsername(account.getUsername())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> accountService.updateAccountByUsername(account.getUsername(), request));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void updateAccountByUsername_existingAccount_updatesAccount() {
        String updatedPassword = account.getPassword() + "Updated";
        String updatedPasswordEncoded = updatedPassword + "Encoded";

        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setPassword(updatedPassword);
        request.setFirstName(account.getProfile().getFirstName() + "Updated");
        request.setLastName(account.getProfile().getLastName() + "Updated");

        Account updatedAccount = AccountTestUtil.mockAccount(1L, account.getUsername(), updatedPasswordEncoded,
                request.getFirstName(), request.getLastName());

        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
        when(passwordEncoder.encode(updatedPassword)).thenReturn(updatedPasswordEncoded);
        when(authenticationService.hasUsername(account.getUsername())).thenReturn(true);
        when(accountRepository.save(account)).thenReturn(updatedAccount);

        AccountData data = accountService.updateAccountByUsername(account.getUsername(), request);

        verify(passwordEncoder, times(1)).encode(updatedPassword);
        verify(account, times(1)).updatePassword(updatedPasswordEncoded);
        verify(account.getProfile(), times(1)).updateFirstName(request.getFirstName());
        verify(account.getProfile(), times(1)).updateLastName(request.getLastName());
        verify(accountRepository, times(1)).save(account);

        assertAccountEquals(updatedAccount, data);
    }

    private static void assertAccountEquals(Account account, AccountData data) {
        assertAll(
                () -> assertEquals(account.getUsername(), data.getUsername()),
                () -> assertEquals(account.getProfile().getFirstName(), data.getFirstName()),
                () -> assertEquals(account.getProfile().getLastName(), data.getLastName())
        );
    }
}