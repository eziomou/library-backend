package pl.zmudzin.library.persistence.jooq.rating;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.rating.Rating;
import pl.zmudzin.library.core.domain.rating.RatingId;
import pl.zmudzin.library.core.domain.rating.RatingRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;
import pl.zmudzin.library.persistence.jooq.PersistenceUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JooqRatingRepositoryTest extends AbstractJooqRepositoryTest<RatingRepository, Rating, RatingId> {

    private final PersistenceUtil persistenceUtil;

    public JooqRatingRepositoryTest() {
        super(new JooqRatingRepository(context));
        persistenceUtil = new PersistenceUtil(context);
    }

    @Override
    protected Rating getEntity() {
        return new Rating(
                RatingId.of(UUID.randomUUID().toString()),
                persistenceUtil.randomMember().getId(),
                persistenceUtil.randomBook().getId(),
                Rating.MAX_VALUE,
                Instant.now()
        );
    }

    @Override
    protected Rating getUpdatedEntity(Rating rating) {
        return rating;
    }

    @Override
    protected void assertEntityEquals(Rating expected, Rating result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getMemberId(), result.getMemberId());
        assertEquals(expected.getBookId(), result.getBookId());
    }

    @AfterEach
    @Override
    protected void afterEach() {
        super.afterEach();
        persistenceUtil.removeAll();
    }

    @Test
    void find_existingRating_returnsRating() {
        Rating rating = getEntity();
        assertFalse(repository.find(rating.getMemberId(), rating.getBookId()).isPresent());
        repository.save(rating);
        assertTrue(repository.find(rating.getMemberId(), rating.getBookId()).isPresent());
    }

    @Test
    void find_notExistingRating_returnsEmpty() {
        Rating rating = getEntity();
        assertFalse(repository.find(rating.getMemberId(), rating.getBookId()).isPresent());
    }

    @Test
    void calculateAverageRating_noRatings_returnsEmpty() {
        assertTrue(repository.calculateAverageRating(persistenceUtil.randomBook().getId()).isEmpty());
    }

    @Test
    void calculateAverageRating_5Ratings_returns3() {
        Book book = persistenceUtil.randomBook();
        Collection<Rating> ratings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Rating rating = new Rating(RatingId.of(UUID.randomUUID().toString()),
                    persistenceUtil.randomMember().getId(), book.getId(), i + 1, Instant.now());
            ratings.add(rating);
        }
        for (int i = 0; i < 5; i++) {
            Rating rating = new Rating(RatingId.of(UUID.randomUUID().toString()),
                    persistenceUtil.randomMember().getId(), persistenceUtil.randomBook().getId(), Rating.MAX_VALUE, Instant.now());
            ratings.add(rating);
        }
        repository.saveAll(ratings);
        repository.calculateAverageRating(book.getId())
                .ifPresentOrElse(value -> assertEquals(3.0, value), Assertions::fail);
    }
}