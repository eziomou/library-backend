package pl.zmudzin.library.persistence.jooq.catalog;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherData;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherQuery;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherReadonlyRepository;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

public class JooqPublisherRepository extends AbstractJooqRepository<Publisher, PublisherId> implements PublisherRepository, PublisherReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Publisher PUBLISHER = Tables.PUBLISHER;

    public JooqPublisherRepository(DSLContext context) {
        super(context, PUBLISHER);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Publisher publisher) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(PUBLISHER.ID, publisher.getId().toString());
        map.put(PUBLISHER.NAME, publisher.getName());
        return map;
    }

    @Override
    protected Publisher mapToDomainModel(Record record) {
        return new Publisher(
                PublisherId.of(record.getValue(PUBLISHER.ID).toString()),
                record.getValue(PUBLISHER.NAME)
        );
    }

    protected PublisherData mapToViewModel(Record record) {
        return new PublisherData(
                record.getValue(PUBLISHER.ID).toString(),
                record.getValue(PUBLISHER.NAME)
        );
    }

    @Override
    protected Condition eq(PublisherId publisherId) {
        return PUBLISHER.ID.eq(UUID.fromString(publisherId.toString()));
    }

    @Override
    public Optional<PublisherData> findById(String publisherId) {
        return Optional.ofNullable(fromStep().where(eq(PublisherId.of(publisherId))).fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<PublisherData> findAllByQuery(PublisherQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        int total = count(conditions.toArray(Condition[]::new));
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(PublisherQuery query) {
        List<Condition> c = new ArrayList<>();
        query.name().ifPresent(n -> c.add(PUBLISHER.NAME.likeIgnoreCase(n)));
        return c;
    }
}
