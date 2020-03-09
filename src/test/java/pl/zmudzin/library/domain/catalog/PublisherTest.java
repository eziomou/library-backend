package pl.zmudzin.library.domain.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class PublisherTest {

    @Test
    void constructor_nullName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Publisher(null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Publisher("n"));
    }
}