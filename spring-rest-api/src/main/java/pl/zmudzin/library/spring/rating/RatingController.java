package pl.zmudzin.library.spring.rating;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.rating.RatingService;
import pl.zmudzin.library.spring.catalog.book.BookController;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping
public class RatingController {

    private final RatingService ratingService;
    private final AuthorizationService authorizationService;

    public RatingController(RatingService ratingService, AuthorizationService authorizationService) {
        this.ratingService = ratingService;
        this.authorizationService = authorizationService;
    }

    @Secured({Role.MEMBER})
    @PostMapping(path = BookController.BASE_PATH + "/{bookId}/ratings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rateBook(@PathVariable String bookId, @Valid @RequestBody RateBookRequest request) {
        ratingService.rateBook(authorizationService.getMemberId(), bookId, request.getValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = BookController.BASE_PATH + "/{bookId}/ratings/average", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateAverageRating(@PathVariable String bookId) {
        Optional<Double> average = ratingService.calculateAverageRating(bookId);
        if (average.isPresent()) {
            return ResponseEntity.ok(new AverageRating(average.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
