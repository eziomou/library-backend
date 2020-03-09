package pl.zmudzin.library.domain.account;

import pl.zmudzin.ddd.annotations.domain.DomainFactoryImpl;

/**
 * @author Piotr Żmudzin
 */
@DomainFactoryImpl
public class AccountFactory {

    private AccountRepository accountRepository;

    public AccountFactory(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String username, String password, String firstName, String lastName) {
        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }
        return new Account(username, password, new Profile(firstName, lastName));
    }
}
