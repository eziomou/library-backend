package pl.zmudzin.library.application.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface RatingService {

    RatingData createRating(RatingCreateRequest request);

    RatingData getRatingById(Long id);

    Page<RatingData> getAllRatings(RatingSearchRequest request, Pageable pageable);

    RatingData updateRatingById(Long id, RatingUpdateRequest request);

    void deleteRatingById(Long id);
}
