package pl.zmudzin.library.core.domain.account;

import pl.zmudzin.library.core.domain.common.StringId;

public class AccountId extends StringId {

    public AccountId(final String value) {
        super(value);
    }

    public static AccountId of(String value) {
        return new AccountId(value);
    }
}
