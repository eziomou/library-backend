package pl.zmudzin.library.ui.rating;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.application.rating.RatingData;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class RatingModelProcessor implements RepresentationModelProcessor<EntityModel<RatingData>> {

    @Override
    public EntityModel<RatingData> process(EntityModel<RatingData> model) {
        model.add(createRatingLink(model.getContent().getId()));
        return model;
    }

    private static Link createRatingLink(Long ratingId) {
        return linkTo(methodOn(RatingController.class).getRatingById(ratingId)).withSelfRel();
    }
}
