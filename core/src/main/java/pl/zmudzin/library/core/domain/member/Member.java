package pl.zmudzin.library.core.domain.member;

import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Member extends AbstractEntity<MemberId> {

    private final Account account;

    public Member(MemberId id, Account account) {
        super(id);
        this.account = Objects.requireNonNull(account);
    }

    public Account getAccount() {
        return account;
    }
}
