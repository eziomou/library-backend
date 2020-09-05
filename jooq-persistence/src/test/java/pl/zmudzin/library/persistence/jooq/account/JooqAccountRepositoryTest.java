package pl.zmudzin.library.persistence.jooq.account;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JooqAccountRepositoryTest extends AbstractJooqRepositoryTest<AccountRepository, Account, AccountId> {

    public JooqAccountRepositoryTest() {
        super(new JooqAccountRepository(context));
    }

    @Override
    protected Account getEntity() {
        return new Account(AccountId.of(UUID.randomUUID().toString()),
                "Foo", "Bar", new Profile("Baz", "Qux"));
    }

    @Override
    protected Account getUpdatedEntity(Account account) {
        account = account.withPassword("Qux");
        account = account.withProfile(new Profile("Baz", "Bar"));
        return account;
    }

    @Override
    protected void assertEntityEquals(Account expected, Account result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getUsername(), result.getUsername());
        assertEquals(expected.getPassword(), result.getPassword());
        assertEquals(expected.getProfile(), result.getProfile());
    }

    @Test
    void findByUsername_existingAccount_returnsAccount() {
        Account account = getEntity();
        repository.save(account);
        assertTrue(repository.findByUsername(account.getUsername()).isPresent());
    }

    @Test
    void findByUsername_notExistingAccount_returnsEmpty() {
        Account account = getEntity();
        assertFalse(repository.findByUsername(account.getUsername()).isPresent());
    }

    @Test
    void existsByUsername_existingAccount_returnsAccount() {
        Account account = getEntity();
        repository.save(account);
        assertTrue(repository.existsByUsername(account.getUsername()));
    }

    @Test
    void existsByUsername_notExistingAccount_returnsEmpty() {
        Account account = getEntity();
        assertFalse(repository.existsByUsername(account.getUsername()));
    }
}