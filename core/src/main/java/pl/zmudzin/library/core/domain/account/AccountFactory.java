package pl.zmudzin.library.core.domain.account;

import java.util.UUID;

public class AccountFactory {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountFactory(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(String username, String password, String firstName, String lastName) {
        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }
        Profile profile = new Profile(firstName, lastName);
        return new Account(new AccountId(UUID.randomUUID().toString()), username, passwordEncoder.encode(password), profile);
    }
}
