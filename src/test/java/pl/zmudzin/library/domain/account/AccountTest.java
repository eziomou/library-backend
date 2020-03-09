package pl.zmudzin.library.domain.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class AccountTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final Profile PROFILE = new Profile("firstName", "lastName");

    @Test
    void constructor_nullUsername_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(null, PASSWORD, PROFILE));
    }

    @Test
    void constructor_nullPassword_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(USERNAME, null, PROFILE));
    }

    @Test
    void constructor_nullProfile_throwsException() {
        assertThrows(NullPointerException.class, () -> new Account(USERNAME, PASSWORD, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Account(USERNAME, PASSWORD, PROFILE));
    }
}