package pl.zmudzin.library.core.application.rating;

import java.util.Optional;

public interface RatingService {

    void rateBook(String memberId, String bookId, int value);

    Optional<Double> calculateAverageRating(String bookId);
}
