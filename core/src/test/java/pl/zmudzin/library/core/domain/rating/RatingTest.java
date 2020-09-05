package pl.zmudzin.library.core.domain.rating;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class RatingTest {

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Rating(null, null, null, 0, null));
    }

    @Test
    void constructor_nullMemberId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Rating(RatingId.of(""), null, null, 0, null));
    }

    @Test
    void constructor_nullBookId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Rating(RatingId.of(""), MemberId.of(""), null, 0, null));
    }

    @Test
    void constructor_tooLowValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(RatingId.of(""), MemberId.of(""), BookId.of(""), Rating.MIN_VALUE - 1, null));
    }

    @Test
    void constructor_tooHighValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(RatingId.of(""), MemberId.of(""), BookId.of(""), Rating.MAX_VALUE + 1, null));
    }

    @Test
    void constructor_nullRateDate_throwsException() {
        assertThrows(NullPointerException.class, () -> new Rating(RatingId.of(""), MemberId.of(""), BookId.of(""), Rating.MAX_VALUE, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Rating(RatingId.of(""), MemberId.of(""), BookId.of(""), Rating.MAX_VALUE, Instant.now()));
    }
}
