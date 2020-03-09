package pl.zmudzin.library.ui.catalog.publisher;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import pl.zmudzin.library.application.catalog.publisher.PublisherData;
import pl.zmudzin.library.ui.catalog.book.BookController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class PublisherModelProcessor implements RepresentationModelProcessor<EntityModel<PublisherData>> {

    @Override
    public EntityModel<PublisherData> process(EntityModel<PublisherData> model) {
        model.add(createPublisherLink(model.getContent().getId(), "self"));
        model.add(createBooksLink(model.getContent().getId()));
        return model;
    }

    public static Link createPublisherLink(Long publisherId, String rel) {
        return linkTo(methodOn(PublisherController.class).getPublisherById(publisherId)).withRel(rel);
    }

    private static Link createBooksLink(Long publisherId) {
        UriComponents components = linkTo(BookController.class).toUriComponentsBuilder()
                .query("publisherId={publisherId}").buildAndExpand(publisherId);

        return new Link(components.toString(), "books");
    }
}
