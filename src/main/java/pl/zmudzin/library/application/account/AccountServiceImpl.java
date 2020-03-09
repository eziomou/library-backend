package pl.zmudzin.library.application.account;

import org.springframework.context.annotation.Lazy;
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
import pl.zmudzin.library.domain.account.AccountRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    public AccountServiceImpl(AccountRepository accountRepository, @Lazy PasswordEncoder passwordEncoder,
                              AuthenticationService authenticationService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Override
    public AccountData getAccountByUsername(String username) {
        Account account = getAccountEntityByUsername(username);
        return map(account);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public AccountData updateAccountByUsername(String username, AccountUpdateRequest request) {
        Account account = getAccountEntityByUsername(username);

        if (request.getPassword() != null) {
            account.updatePassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstName() != null) {
            account.getProfile().updateFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            account.getProfile().updateLastName(request.getLastName());
        }
        account = accountRepository.save(account);
        return map(account);
    }

    private Account getAccountEntityByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!authenticationService.hasUsername(username) &&
                !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return account;
    }

    private AccountData map(Account account) {
        AccountData data = new AccountData();
        data.setUsername(account.getUsername());
        data.setFirstName(account.getProfile().getFirstName());
        data.setLastName(account.getProfile().getLastName());
        return data;
    }
}
