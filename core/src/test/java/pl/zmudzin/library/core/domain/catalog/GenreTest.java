package pl.zmudzin.library.core.domain.catalog;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GenreTest {

    private final GenreId genreId = GenreId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Genre(null, null));
    }

    @Test
    void constructor_nullName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Genre(genreId, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Genre genre = new Genre(genreId, "Foo");
        assertEquals("Foo", genre.getName());
    }
}