package pl.zmudzin.library.core.domain.librarian;

import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.common.Repository;

import java.util.List;
import java.util.Optional;

public interface LibrarianRepository extends Repository<Librarian, LibrarianId> {

    Optional<Librarian> findByUsername(String username);

    boolean existsByAccountId(AccountId accountId);

    List<Librarian> findAll(Pagination pagination);
}
