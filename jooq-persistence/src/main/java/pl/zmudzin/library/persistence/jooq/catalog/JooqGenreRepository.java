package pl.zmudzin.library.persistence.jooq.catalog;

import org.jooq.*;
import pl.zmudzin.library.core.application.catalog.genre.GenreData;
import pl.zmudzin.library.core.application.catalog.genre.GenreQuery;
import pl.zmudzin.library.core.application.catalog.genre.GenreReadonlyRepository;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

public class JooqGenreRepository extends AbstractJooqRepository<Genre, GenreId> implements GenreRepository, GenreReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Genre GENRE = Tables.GENRE;

    public JooqGenreRepository(DSLContext context) {
        super(context, GENRE);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Genre genre) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(GENRE.ID, genre.getId().toString());
        map.put(GENRE.NAME, genre.getName());
        return map;
    }

    @Override
    protected Genre mapToDomainModel(Record record) {
        return new Genre(
                GenreId.of(record.getValue(GENRE.ID).toString()),
                record.getValue(GENRE.NAME)
        );
    }

    protected GenreData mapToViewModel(Record record) {
        return new GenreData(
                record.getValue(GENRE.ID).toString(),
                record.getValue(GENRE.NAME)
        );
    }

    @Override
    protected Condition eq(GenreId genreId) {
        return GENRE.ID.eq(UUID.fromString(genreId.toString()));
    }

    @Override
    public Optional<GenreData> findById(String genreId) {
        return Optional.ofNullable(fromStep().where(eq(GenreId.of(genreId))).fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<GenreData> findAllByQuery(GenreQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        int total = count(conditions.toArray(Condition[]::new));
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(GenreQuery query) {
        List<Condition> c = new ArrayList<>();
        query.name().ifPresent(n -> c.add(GENRE.NAME.likeIgnoreCase(n)));
        return c;
    }
}
