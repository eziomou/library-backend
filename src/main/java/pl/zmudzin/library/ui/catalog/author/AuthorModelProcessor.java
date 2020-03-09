package pl.zmudzin.library.ui.catalog.author;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import pl.zmudzin.library.application.catalog.author.AuthorData;
import pl.zmudzin.library.ui.catalog.book.BookController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class AuthorModelProcessor implements RepresentationModelProcessor<EntityModel<AuthorData>> {

    @Override
    public EntityModel<AuthorData> process(EntityModel<AuthorData> model) {
        model.add(createAuthorLink(model.getContent().getId(), "self"));
        model.add(createBooksLink(model.getContent().getId()));
        return model;
    }

    public static Link createAuthorLink(Long authorId, String rel) {
        return linkTo(methodOn(AuthorController.class).getAuthorById(authorId)).withRel(rel);
    }

    private static Link createBooksLink(Long authorId) {
        UriComponents components = linkTo(BookController.class).toUriComponentsBuilder()
                .query("authorId={authorId}").buildAndExpand(authorId);

        return new Link(components.toString(), "books");
    }
}
