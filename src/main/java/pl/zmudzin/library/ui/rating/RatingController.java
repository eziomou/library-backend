package pl.zmudzin.library.ui.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.rating.*;
import pl.zmudzin.library.ui.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/ratings", produces = "application/json")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<RatingData>> createRating(@Valid @RequestBody RatingCreateRequest request) {
        RatingData data = ratingService.createRating(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RatingData>> getRatingById(@PathVariable Long id) {
        RatingData data = ratingService.getRatingById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<RatingData>>> getAllRatings(@Valid RatingSearchRequest request,
                                                               @PageableDefault Pageable pageable) {
        Page<RatingData> data = ratingService.getAllRatings(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<RatingData>> updateRatingById(@PathVariable Long id,
                                                      @Valid @RequestBody RatingUpdateRequest request) {
        RatingData data = ratingService.updateRatingById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }
}
