package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.catalog.book.BookCreateRequest;
import pl.zmudzin.library.domain.catalog.*;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class BookTestUtil {

    public static final Long ID = 1L;
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final LocalDate PUBLICATION_DATE = LocalDate.now();

    public static BookCreateRequest getBookCreateRequest(Long authorId, Long genreId, Long publisherId) {
        return getBookCreateRequest(TITLE, authorId, genreId, publisherId, DESCRIPTION, PUBLICATION_DATE);
    }

    public static BookCreateRequest getBookCreateRequest(String title, Long authorId, Long genreId, Long publisherId,
                                                         String description, LocalDate publicationDate) {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle(title);
        request.setAuthorId(authorId);
        request.setGenreId(genreId);
        request.setPublisherId(publisherId);
        request.setDescription(description);
        request.setPublicationDate(publicationDate);
        return request;
    }

    public static Book createBook(BookRepository bookRepository, AuthorRepository authorRepository,
                                  GenreRepository genreRepository, PublisherRepository publisherRepository) {
        return createBook(bookRepository, AuthorTestUtil.createAuthor(authorRepository), GenreTestUtil.createGenre(genreRepository),
                PublisherTestUtil.createPublisher(publisherRepository));
    }

    public static Book createBook(BookRepository bookRepository, Author author, Genre genre, Publisher publisher) {
        Book book = new Book(TITLE, author, genre, publisher, DESCRIPTION, PUBLICATION_DATE);
        return bookRepository.save(book);
    }

    public static Book mockBook() {
        return mockBook(ID, TITLE, AuthorTestUtil.mockAuthor(), GenreTestUtil.mockGenre(), PublisherTestUtil.mockPublisher(),
                DESCRIPTION, PUBLICATION_DATE);
    }

    public static Book mockBook(Long id, String title, Author author, Genre genre, Publisher publisher, String description,
                                LocalDate publicationDate) {
        Book book = Mockito.mock(Book.class, withSettings().lenient());
        when(book.getId()).thenReturn(id);
        when(book.getTitle()).thenReturn(title);
        when(book.getAuthor()).thenReturn(author);
        when(book.getGenre()).thenReturn(genre);
        when(book.getPublisher()).thenReturn(publisher);
        when(book.getDescription()).thenReturn(description);
        when(book.getPublicationDate()).thenReturn(publicationDate);
        return book;
    }
}
