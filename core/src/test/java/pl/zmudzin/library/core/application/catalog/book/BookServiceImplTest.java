package pl.zmudzin.library.core.application.catalog.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private static final Book BOOK = Book.builder()
            .id(BookId.of(""))
            .title("Foo")
            .description("Bar")
            .publicationDate(Instant.now())
            .author(new Author(AuthorId.of(""), "Foo", "Bar"))
            .genre(new Genre(GenreId.of(""), "Foo"))
            .publisher(new Publisher(PublisherId.of(""), "Foo"))
            .build();

    private static final AddBookCommand ADD_COMMAND = AddBookCommand.builder()
            .title("Foo")
            .description("Bar")
            .publicationDate(Instant.now().toString())
            .authorId(AuthorId.of("").toString())
            .genreId(GenreId.of("").toString())
            .publisherId(PublisherId.of("").toString())
            .build();

    private static final UpdateBookCommand UPDATE_COMMAND = UpdateBookCommand.builder()
            .id(BookId.of("").toString())
            .title("Baz")
            .description("Qux")
            .publicationDate(Instant.now().toString())
            .authorId(AuthorId.of("").toString())
            .genreId(GenreId.of("").toString())
            .publisherId(PublisherId.of("").toString())
            .build();

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookReadonlyRepository bookReadonlyRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void addBook_validBook_addsBook() {
        when(authorRepository.find(AuthorId.of(ADD_COMMAND.getAuthorId()))).thenReturn(Optional.of(BOOK.getAuthor()));
        when(genreRepository.find(GenreId.of(ADD_COMMAND.getGenreId()))).thenReturn(Optional.of(BOOK.getGenre()));
        when(publisherRepository.find(PublisherId.of(ADD_COMMAND.getPublisherId()))).thenReturn(Optional.of(BOOK.getPublisher()));

        bookService.addBook(ADD_COMMAND);

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateBook_existingBook_updatesBook() {
        when(bookRepository.find(BOOK.getId())).thenReturn(Optional.of(BOOK));

        when(authorRepository.find(AuthorId.of(ADD_COMMAND.getAuthorId()))).thenReturn(Optional.of(BOOK.getAuthor()));
        when(genreRepository.find(GenreId.of(ADD_COMMAND.getGenreId()))).thenReturn(Optional.of(BOOK.getGenre()));
        when(publisherRepository.find(PublisherId.of(ADD_COMMAND.getPublisherId()))).thenReturn(Optional.of(BOOK.getPublisher()));

        bookService.updateBook(UPDATE_COMMAND);

        verify(bookRepository, times(1)).update(any());
    }

    @Test
    void updateBook_notExistingBook_throwsException() {
        when(bookRepository.find(BOOK.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(UPDATE_COMMAND));

        verify(bookRepository, times(0)).update(any());
    }

    @Test
    void removeBook_existingBook_deletesBook() {
        when(bookRepository.find(BOOK.getId())).thenReturn(Optional.of(BOOK));

        bookService.removeBook(BOOK.getId().toString());

        verify(bookRepository, times(1)).delete(any());
    }

    @Test
    void removeBook_notExistingBook_throwsException() {
        when(bookRepository.find(BOOK.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.removeBook(BOOK.getId().toString()));

        verify(bookRepository, times(0)).delete(any());
    }
}