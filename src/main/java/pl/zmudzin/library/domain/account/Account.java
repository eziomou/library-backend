package pl.zmudzin.library.domain.account;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@AggregateRoot
@Entity
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Profile profile;

    @SuppressWarnings("unused")
    protected Account() {
    }

    Account(String username, String password, Profile profile) {
        setUsername(username);
        setPassword(password);
        setProfile(profile);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = Objects.requireNonNull(username);
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = Objects.requireNonNull(password);
    }

    public void updatePassword(String password) {
        setPassword(password);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = Objects.requireNonNull(profile);
    }
}
