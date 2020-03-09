package pl.zmudzin.library.ui.catalog.book;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.application.catalog.book.BookData;
import pl.zmudzin.library.ui.catalog.author.AuthorModelProcessor;
import pl.zmudzin.library.ui.catalog.genre.GenreModelProcessor;
import pl.zmudzin.library.ui.catalog.publisher.PublisherModelProcessor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class BookModelProcessor implements RepresentationModelProcessor<EntityModel<BookData>> {

    @Override
    public EntityModel<BookData> process(EntityModel<BookData> model) {
        model.add(createBookLink(model.getContent().getId(), "self"));
        model.add(AuthorModelProcessor.createAuthorLink(model.getContent().getAuthor().getId(), "author"));
        model.add(GenreModelProcessor.createGenreLink(model.getContent().getGenre().getId(), "genre"));
        model.add(PublisherModelProcessor.createPublisherLink(model.getContent().getPublisher().getId(), "publisher"));
        return model;
    }

    public static Link createBookLink(Long bookId, String rel) {
        return linkTo(methodOn(BookController.class).getBookById(bookId)).withRel(rel);
    }
}
