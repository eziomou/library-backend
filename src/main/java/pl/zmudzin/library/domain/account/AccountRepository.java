package pl.zmudzin.library.domain.account;

import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    void delete(Account account);
}
