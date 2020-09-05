package pl.zmudzin.library.core.application.account;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.account.*;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountData getAccountById(String accountId) {
        Account account = accountRepository.find(AccountId.of(accountId))
                .orElseThrow(() -> newAccountNotFoundException(accountId));
        return map(account);
    }

    private AccountData map(Account account) {
        return AccountData.builder()
                .username(account.getUsername())
                .firstName(account.getProfile().getFirstName())
                .lastName(account.getProfile().getLastName())
                .build();
    }

    public void updatePassword(String accountId, String password) {
        Account account = accountRepository.find(AccountId.of(accountId))
                .orElseThrow(() -> newAccountNotFoundException(accountId));

        account = account.withPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    public void updateProfile(String accountId, String firstName, String lastName) {
        Account account = accountRepository.find(AccountId.of(accountId))
                .orElseThrow(() -> newAccountNotFoundException(accountId));

        account = account.withProfile(new Profile(firstName, lastName));
        accountRepository.save(account);
    }

    private NotFoundException newAccountNotFoundException(String username) {
        return new NotFoundException("Account not found: " + username);
    }
}
