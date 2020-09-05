package pl.zmudzin.library.persistence.jooq.catalog;

import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqPublisherRepositoryTest extends AbstractJooqRepositoryTest<PublisherRepository, Publisher, PublisherId> {

    public JooqPublisherRepositoryTest() {
        super(new JooqPublisherRepository(context));
    }

    @Override
    protected Publisher getEntity() {
        return new Publisher(PublisherId.of(UUID.randomUUID().toString()), "Foo");
    }

    @Override
    protected Publisher getUpdatedEntity(Publisher publisher) {
        publisher = publisher.withName("Bar");
        return publisher;
    }

    @Override
    protected void assertEntityEquals(Publisher expected, Publisher result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
    }
}
