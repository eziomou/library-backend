package pl.zmudzin.library.persistence.jooq.catalog;

import org.jooq.*;
import pl.zmudzin.library.core.application.catalog.book.BookData;
import pl.zmudzin.library.core.application.catalog.book.BookQuery;
import pl.zmudzin.library.core.application.catalog.book.BookReadonlyRepository;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.time.ZoneOffset;
import java.util.*;

import static org.jooq.impl.DSL.condition;

public class JooqBookRepository extends AbstractJooqRepository<Book, BookId> implements BookRepository, BookReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Book BOOK = Tables.BOOK;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Author AUTHOR = Tables.AUTHOR;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Genre GENRE = Tables.GENRE;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Publisher PUBLISHER = Tables.PUBLISHER;

    public JooqBookRepository(DSLContext context) {
        super(context, Tables.BOOK);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Book book) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(BOOK.ID, book.getId().toString());
        map.put(BOOK.TITLE, book.getTitle());
        map.put(BOOK.DESCRIPTION, book.getDescription());
        map.put(BOOK.PUBLICATION_DATE, book.getPublicationDate().toString());
        map.put(BOOK.AUTHOR_ID, book.getAuthor().getId().toString());
        map.put(BOOK.GENRE_ID, book.getGenre().getId().toString());
        map.put(BOOK.PUBLISHER_ID, book.getPublisher().getId().toString());
        return map;
    }

    @Override
    protected Book mapToDomainModel(Record record) {
        return Book.builder()
                .id(BookId.of(record.get(BOOK.ID).toString()))
                .title(record.get(BOOK.TITLE))
                .description(record.get(BOOK.DESCRIPTION))
                .publicationDate(record.get(BOOK.PUBLICATION_DATE).toInstant())
                .author(new Author(
                        AuthorId.of(record.get(AUTHOR.ID).toString()),
                        record.get(AUTHOR.FIRST_NAME),
                        record.get(AUTHOR.LAST_NAME)))
                .genre(new Genre(
                        GenreId.of(record.get(GENRE.ID).toString()),
                        record.get(GENRE.NAME)))
                .publisher(new Publisher(
                        PublisherId.of(record.get(PUBLISHER.ID).toString()),
                        record.get(PUBLISHER.NAME)))
                .build();
    }

    protected BookData mapToViewModel(Record record) {
        return BookData.builder()
                .id(record.getValue(BOOK.ID).toString())
                .title(record.getValue(BOOK.TITLE))
                .description(record.getValue(BOOK.DESCRIPTION))
                .publicationDate(record.getValue(BOOK.PUBLICATION_DATE).toString())
                .author(builder -> builder
                        .id(record.getValue(AUTHOR.ID).toString())
                        .firstName(record.getValue(AUTHOR.FIRST_NAME))
                        .lastName(record.getValue(AUTHOR.LAST_NAME)))
                .genre(builder -> builder
                        .id(record.getValue(GENRE.ID).toString())
                        .name(record.getValue(GENRE.NAME)))
                .publisher(builder -> builder
                        .id(record.getValue(PUBLISHER.ID).toString())
                        .name(record.getValue(PUBLISHER.NAME)))
                .build();
    }

    @Override
    protected Condition eq(BookId bookId) {
        return BOOK.ID.eq(UUID.fromString(bookId.toString()));
    }

    @Override
    protected SelectJoinStep<? extends Record> joinStep(SelectJoinStep<? extends Record> joinStep) {
        return joinStep
                .join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                .join(GENRE).on(BOOK.GENRE_ID.eq(GENRE.ID))
                .join(PUBLISHER).on(BOOK.PUBLISHER_ID.eq(PUBLISHER.ID));
    }

    @Override
    public Optional<BookData> findById(String bookId) {
        return Optional.ofNullable(joinStep().where(BOOK.ID.eq(UUID.fromString(bookId))).fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<BookData> findAllByQuery(BookQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        int total = count(conditions.toArray(Condition[]::new));
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(BookQuery query) {
        List<Condition> c = new ArrayList<>();
        query.phrase().ifPresent(p -> c.add(condition("to_tsvector('english', " +
                BOOK.TITLE.toString() + " || ' ' || " +
                BOOK.DESCRIPTION.toString() + " || ' ' || " +
                AUTHOR.FIRST_NAME.toString() + " || ' ' || " +
                AUTHOR.LAST_NAME.toString() + " || ' ' || " +
                GENRE.NAME.toString() + " || ' ' || " +
                PUBLISHER.NAME.toString() +
                ") @@ plainto_tsquery('english', ?)", p)));
        query.getTitle().ifPresent(t -> c.add(BOOK.TITLE.eq(t)));
        query.getDescription().ifPresent(d -> c.add(BOOK.DESCRIPTION.eq(d)));
        query.getMinPublicationDate().ifPresent(d -> c.add(BOOK.PUBLICATION_DATE.ge(d.atOffset(ZoneOffset.UTC))));
        query.getMaxPublicationDate().ifPresent(d -> c.add(BOOK.PUBLICATION_DATE.le(d.atOffset(ZoneOffset.UTC))));
        query.getAuthorId().ifPresent(i -> c.add(BOOK.AUTHOR_ID.eq(UUID.fromString(i.toString()))));
        query.getGenreId().ifPresent(i -> c.add(BOOK.GENRE_ID.eq(UUID.fromString(i.toString()))));
        query.getPublisherId().ifPresent(i -> c.add(BOOK.PUBLISHER_ID.eq(UUID.fromString(i.toString()))));
        return c;
    }
}
