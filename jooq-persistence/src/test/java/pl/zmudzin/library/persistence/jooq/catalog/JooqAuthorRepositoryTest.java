package pl.zmudzin.library.persistence.jooq.catalog;

import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqAuthorRepositoryTest extends AbstractJooqRepositoryTest<AuthorRepository, Author, AuthorId> {

    public JooqAuthorRepositoryTest() {
        super(new JooqAuthorRepository(context));
    }

    @Override
    protected Author getEntity() {
        return new Author(AuthorId.of(UUID.randomUUID().toString()), "Foo", "Bar");
    }

    @Override
    protected Author getUpdatedEntity(Author author) {
        author = author.withFirstName("Baz");
        author = author.withLastName("Qux");
        return author;
    }

    @Override
    protected void assertEntityEquals(Author expected, Author result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());
    }
}