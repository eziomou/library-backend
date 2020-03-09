package pl.zmudzin.library.domain.librarian;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.account.Account;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Piotr Żmudzin
 */
class LibrarianTest {

    private static Account account = Mockito.mock(Account.class);

    @Test
    void constructor_nullAccount_throwsException() {
        assertThrows(NullPointerException.class, () -> new Librarian(null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Librarian(account));
    }
}