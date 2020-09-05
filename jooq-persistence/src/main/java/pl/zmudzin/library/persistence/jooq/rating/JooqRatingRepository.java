package pl.zmudzin.library.persistence.jooq.rating;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.rating.Rating;
import pl.zmudzin.library.core.domain.rating.RatingId;
import pl.zmudzin.library.core.domain.rating.RatingRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.avg;

public class JooqRatingRepository extends AbstractJooqRepository<Rating, RatingId> implements RatingRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Rating RATING = Tables.RATING;

    public JooqRatingRepository(DSLContext context) {
        super(context, RATING);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Rating rating) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(RATING.ID, rating.getId().toString());
        map.put(RATING.MEMBER_ID, rating.getMemberId().toString());
        map.put(RATING.BOOK_ID, rating.getBookId().toString());
        map.put(RATING.VALUE, rating.getValue());
        map.put(RATING.RATE_DATE, rating.getRateDate().toString());
        return map;
    }

    @Override
    protected Rating mapToDomainModel(Record record) {
        return new Rating(
                RatingId.of(record.get(RATING.ID).toString()),
                MemberId.of(record.get(RATING.MEMBER_ID).toString()),
                BookId.of(record.get(RATING.BOOK_ID).toString()),
                record.get(RATING.VALUE),
                record.get(RATING.RATE_DATE).toInstant()
        );
    }

    @Override
    protected Condition eq(RatingId ratingId) {
        return RATING.ID.eq(UUID.fromString(ratingId.toString()));
    }

    @Override
    public Optional<Rating> find(MemberId memberId, BookId bookId) {
        return find(RATING.MEMBER_ID.eq(UUID.fromString(memberId.toString())),
                RATING.BOOK_ID.eq(UUID.fromString(bookId.toString())));
    }

    @Override
    public Optional<Double> calculateAverageRating(BookId bookId) {
        return context.select(avg(RATING.VALUE))
                .from(RATING)
                .where(RATING.BOOK_ID.eq(UUID.fromString(bookId.toString())))
                .fetchOne(record -> Optional.ofNullable(record.get(0, Double.class)));
    }
}
