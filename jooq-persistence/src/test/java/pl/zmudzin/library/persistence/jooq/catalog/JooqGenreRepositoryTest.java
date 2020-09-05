package pl.zmudzin.library.persistence.jooq.catalog;

import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqGenreRepositoryTest extends AbstractJooqRepositoryTest<GenreRepository, Genre, GenreId> {

    public JooqGenreRepositoryTest() {
        super(new JooqGenreRepository(context));
    }

    @Override
    protected Genre getEntity() {
        return new Genre(GenreId.of(UUID.randomUUID().toString()), "Foo");
    }

    @Override
    protected Genre getUpdatedEntity(Genre genre) {
        genre = genre.withName("Bar");
        return genre;
    }

    @Override
    protected void assertEntityEquals(Genre expected, Genre result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
    }
}