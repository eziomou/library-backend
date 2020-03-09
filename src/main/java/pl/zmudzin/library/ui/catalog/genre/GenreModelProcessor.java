package pl.zmudzin.library.ui.catalog.genre;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import pl.zmudzin.library.application.catalog.genre.GenreData;
import pl.zmudzin.library.ui.catalog.book.BookController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class GenreModelProcessor implements RepresentationModelProcessor<EntityModel<GenreData>> {

    @Override
    public EntityModel<GenreData> process(EntityModel<GenreData> model) {
        model.add(createGenreLink(model.getContent().getId(), "self"));
        model.add(createBooksLink(model.getContent().getId()));
        return model;
    }

    public static Link createGenreLink(Long genreId, String rel) {
        return linkTo(methodOn(GenreController.class).getGenreById(genreId)).withRel(rel);
    }

    private static Link createBooksLink(Long genreId) {
        UriComponents components = linkTo(BookController.class).toUriComponentsBuilder()
                .query("genreId={genreId}").buildAndExpand(genreId);

        return new Link(components.toString(), "books");
    }
}
