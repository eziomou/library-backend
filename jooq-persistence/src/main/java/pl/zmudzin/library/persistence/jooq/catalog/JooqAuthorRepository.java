package pl.zmudzin.library.persistence.jooq.catalog;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import pl.zmudzin.library.core.application.catalog.author.AuthorData;
import pl.zmudzin.library.core.application.catalog.author.AuthorQuery;
import pl.zmudzin.library.core.application.catalog.author.AuthorReadonlyRepository;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

import static org.jooq.impl.DSL.concat;

public class JooqAuthorRepository extends AbstractJooqRepository<Author, AuthorId> implements AuthorRepository, AuthorReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Author AUTHOR = Tables.AUTHOR;

    public JooqAuthorRepository(DSLContext context) {
        super(context, AUTHOR);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Author author) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(AUTHOR.ID, author.getId().toString());
        map.put(AUTHOR.FIRST_NAME, author.getFirstName());
        map.put(AUTHOR.LAST_NAME, author.getLastName());
        return map;
    }

    @Override
    protected Author mapToDomainModel(Record record) {
        return new Author(
                AuthorId.of(record.getValue(AUTHOR.ID).toString()),
                record.getValue(AUTHOR.FIRST_NAME),
                record.getValue(AUTHOR.LAST_NAME)
        );
    }

    protected AuthorData mapToViewModel(Record record) {
        return new AuthorData(
                record.getValue(AUTHOR.ID).toString(),
                record.getValue(AUTHOR.FIRST_NAME),
                record.getValue(AUTHOR.LAST_NAME)
        );
    }

    @Override
    protected Condition eq(AuthorId authorId) {
        return AUTHOR.ID.eq(UUID.fromString(authorId.toString()));
    }

    @Override
    public Optional<AuthorData> findById(String authorId) {
        return Optional.ofNullable(fromStep().where(eq(AuthorId.of(authorId))).fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<AuthorData> findAllByQuery(AuthorQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        int total = count(conditions.toArray(Condition[]::new));
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(AuthorQuery query) {
        List<Condition> c = new ArrayList<>();
        query.firstName().ifPresent(fn -> c.add(AUTHOR.FIRST_NAME.likeIgnoreCase(fn)));
        query.lastName().ifPresent(ln -> c.add(AUTHOR.LAST_NAME.likeIgnoreCase(ln)));
        query.fullName().ifPresent(fn -> c.add(concat(AUTHOR.FIRST_NAME, concat(" ", AUTHOR.LAST_NAME)).eq(fn)));
        return c;
    }
}
