package pl.zmudzin.library.domain.rating;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.util.ReflectionUtil;

import java.time.Clock;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Piotr Żmudzin
 */
class RatingDomainServiceTest {

    private static final Member MEMBER = Mockito.mock(Member.class);
    private static final Book BOOK = Mockito.mock(Book.class);

    private RatingDomainService ratingDomainService = new RatingDomainService(Clock.systemDefaultZone());

    @Test
    void rate_memberNotRatedYet_ratesBook() {
        int value = 5;

        Rating rating = ratingDomainService.rate(MEMBER, BOOK, value);

        assertAll(
                () -> assertEquals(MEMBER, rating.getMember()),
                () -> assertEquals(BOOK, rating.getBook()),
                () -> assertEquals(value, rating.getValue())
        );
    }

    @Test
    void rate_memberRatedAlready_updatesPreviousRate() throws Exception {
        int value = 4;
        Rating previousRating = createRating(1L, value);

        when(MEMBER.getRatings()).thenReturn(Collections.singletonList(previousRating));
        when(BOOK.getRatings()).thenReturn(Collections.singletonList(previousRating));

        int newValue = 5;
        Rating rating = ratingDomainService.rate(MEMBER, BOOK, newValue);

        assertAll(
                () -> assertEquals(previousRating.getId(), rating.getId()),
                () -> assertEquals(newValue, rating.getValue())
        );
    }

    private static Rating createRating(Long id, int value) throws Exception {
        Rating rating = ReflectionUtil.getInstance(Rating.class);
        ReflectionUtil.setField(rating, "id", id);
        ReflectionUtil.setField(rating, "member", MEMBER);
        ReflectionUtil.setField(rating, "book", BOOK);
        ReflectionUtil.setField(rating, "value", value);
        return rating;
    }
}