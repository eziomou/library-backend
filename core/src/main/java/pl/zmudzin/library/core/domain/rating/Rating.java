package pl.zmudzin.library.core.domain.rating;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.AbstractEntity;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;
import java.util.Objects;

public class Rating extends AbstractEntity<RatingId> {

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 5;

    private final MemberId memberId;
    private final BookId bookId;
    private final int value;
    private final Instant rateDate;

    public Rating(RatingId id, MemberId memberId, BookId bookId, int value, Instant rateDate) {
        super(id);
        this.memberId = Objects.requireNonNull(memberId);
        this.bookId = Objects.requireNonNull(bookId);
        this.value = validateValue(value);
        this.rateDate = Objects.requireNonNull(rateDate);
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public BookId getBookId() {
        return bookId;
    }

    public int getValue() {
        return value;
    }

    private int validateValue(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between " + MIN_VALUE + " and " + MAX_VALUE);
        }
        return value;
    }

    public Instant getRateDate() {
        return rateDate;
    }
}
