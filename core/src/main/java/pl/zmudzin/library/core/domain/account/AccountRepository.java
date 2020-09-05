package pl.zmudzin.library.core.domain.account;

import pl.zmudzin.library.core.domain.common.Repository;

import java.util.Optional;

public interface AccountRepository extends Repository<Account, AccountId> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);
}
