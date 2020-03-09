package pl.zmudzin.library.domain.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class AuthorTest {

    @Test
    void constructor_nullFirstName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Author(null, "ln"));
    }

    @Test
    void constructor_nullLastName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Author("fn", null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Author("fn", "ln"));
    }
}