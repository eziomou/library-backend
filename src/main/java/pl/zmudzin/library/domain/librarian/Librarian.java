package pl.zmudzin.library.domain.librarian;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@AggregateRoot
@Entity
public class Librarian extends BaseEntity {

    @OneToOne(optional = false)
    private Account account;

    @SuppressWarnings("unused")
    protected Librarian() {
    }

    public Librarian(Account account) {
        setAccount(account);
    }

    public Account getAccount() {
        return account;
    }

    private void setAccount(Account account) {
        this.account = Objects.requireNonNull(account);
    }
}
