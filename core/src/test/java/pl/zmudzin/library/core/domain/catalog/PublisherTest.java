package pl.zmudzin.library.core.domain.catalog;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PublisherTest {

    private final PublisherId publisherId = PublisherId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Publisher(null, null));
    }

    @Test
    void constructor_nullName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Publisher(publisherId, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Publisher publisher = new Publisher(publisherId, "Foo");
        assertEquals("Foo", publisher.getName());
    }
}