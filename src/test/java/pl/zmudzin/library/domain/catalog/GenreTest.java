package pl.zmudzin.library.domain.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class GenreTest {

    @Test
    void constructor_nullName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Genre(null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Genre("n"));
    }
}