package pl.zmudzin.library.domain.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class AccountFactoryTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountFactory accountFactory;

    @Test
    void createAccount_existingUsername_throwsException() {
        when(accountRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> accountFactory.createAccount(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME));
    }

    @Test
    void createAccount_nonExistingUsername_throwsException() {
        when(accountRepository.existsByUsername(USERNAME)).thenReturn(false);

        Account account = accountFactory.createAccount(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME);

        assertAll(
                () -> assertEquals(account.getUsername(), USERNAME),
                () -> assertEquals(account.getPassword(), PASSWORD)
        );
    }
}