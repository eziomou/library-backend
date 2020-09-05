package pl.zmudzin.library.core.domain.account;

import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Account extends AbstractEntity<AccountId> {

    private final String username;
    private final String password;
    private final Profile profile;

    public Account(AccountId id, String username, String password, Profile profile) {
        super(id);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.profile = Objects.requireNonNull(profile);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Account withPassword(String password) {
        return new Account(getId(), username, password, profile);
    }

    public Profile getProfile() {
        return profile;
    }

    public Account withProfile(Profile profile) {
        return new Account(getId(), username, password, profile);
    }
}
