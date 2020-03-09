package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.catalog.Author;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface AccountJpaRepository extends AccountRepository, JpaRepository<Account, Long>,
        JpaSpecificationExecutor<Author> {
}
