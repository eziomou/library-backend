package pl.zmudzin.library.domain.rating;

import pl.zmudzin.ddd.annotations.domain.DomainServiceImpl;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author Piotr Żmudzin
 */
@DomainServiceImpl
public class RatingDomainService {

    private Clock clock;

    public RatingDomainService(Clock clock) {
        this.clock = clock;
    }

    public Rating rate(Member member, Book book, int value) {
        Rating rating = findExistingRating(member, book);
        LocalDateTime ratingDate = LocalDateTime.now(clock);

        if (rating != null) {
            rating.updateValue(value);
        } else {
            rating = new Rating(member, book, value, ratingDate);
        }
        return rating;
    }

    private Rating findExistingRating(Member member, Book book) {
        for (Rating r : member.getRatings()) {
            if (r.getBook().equals(book)) {
                return r;
            }
        }
        return null;
    }
}
