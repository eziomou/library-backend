package pl.zmudzin.library.core.domain.librarian;

import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Librarian extends AbstractEntity<LibrarianId> {

    private final Account account;

    public Librarian(LibrarianId id, Account account) {
        super(id);
        this.account = Objects.requireNonNull(account);
    }

    public Account getAccount() {
        return account;
    }
}
