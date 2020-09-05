package pl.zmudzin.library.core.domain.catalog;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorTest {

    private final AuthorId authorId = AuthorId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Author(null, null, "Foo"));
    }

    @Test
    void constructor_nullFirstName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Author(authorId, null, "Foo"));
    }

    @Test
    void constructor_nullLastName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Author(authorId, "Foo", null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Author author = new Author(authorId, "Foo", "Bar");
        assertEquals("Foo", author.getFirstName());
        assertEquals("Bar", author.getLastName());
        assertEquals("Foo Bar", author.getFullName());
    }
}