package pl.zmudzin.library.core.domain.rating;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.Repository;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.util.Optional;

public interface RatingRepository extends Repository<Rating, RatingId> {

    Optional<Rating> find(MemberId memberId, BookId bookId);

    Optional<Double> calculateAverageRating(BookId bookId);
}
