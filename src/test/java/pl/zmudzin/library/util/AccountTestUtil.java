package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountFactory;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.account.Profile;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class AccountTestUtil {

    public static final Long ID = 1L;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public static Account getAccount(AccountRepository accountRepository) {
        return accountRepository.findByUsername(USERNAME).orElseGet(() -> {
            AccountFactory accountFactory = new AccountFactory(accountRepository);
            Account account = accountFactory.createAccount(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME);
            return accountRepository.save(account);
        });
    }

    public static Account mockAccount() {
        return mockAccount(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME);
    }

    public static Account mockAccount(Long id, String username, String password, String firstName, String lastName) {
        Account account = Mockito.mock(Account.class, withSettings().lenient());
        when(account.getId()).thenReturn(id);
        when(account.getUsername()).thenReturn(username);
        when(account.getPassword()).thenReturn(password);

        Profile profile = Mockito.mock(Profile.class, withSettings().lenient());
        when(profile.getFirstName()).thenReturn(firstName);
        when(profile.getLastName()).thenReturn(lastName);

        when(account.getProfile()).thenReturn(profile);

        return account;
    }
}
