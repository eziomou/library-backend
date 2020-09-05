package pl.zmudzin.library.core.domain.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private final AccountId accountId = AccountId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(null, null, null, null));
    }

    @Test
    void constructor_nullUsername_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(accountId, null, null, null));
    }

    @Test
    void constructor_nullPassword_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(accountId, "Foo", null, null));
    }

    @Test
    void constructor_nullProfile_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(accountId, "Foo", "Bar", null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Account account = new Account(accountId, "Foo", "Bar", new Profile("Baz", "Qux"));
        assertEquals("Foo", account.getUsername());
        assertEquals("Bar", account.getPassword());
        assertEquals(new Profile("Baz", "Qux"), account.getProfile());
    }

    @Test
    void equals_sameAccount_returnsTrue() {
        Account first = new Account(accountId, "Foo", "Bar", new Profile("Baz", "Qux"));
        Account second = new Account(accountId, "Foo", "Bar", new Profile("Baz", "Qux"));
        assertEquals(first, second);
    }
}