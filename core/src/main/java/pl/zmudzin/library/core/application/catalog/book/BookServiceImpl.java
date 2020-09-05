package pl.zmudzin.library.core.application.catalog.book;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
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
import pl.zmudzin.library.core.application.common.Pagination;

import java.time.Instant;
import java.util.UUID;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookReadonlyRepository bookReadonlyRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;

    public BookServiceImpl(BookRepository bookRepository, BookReadonlyRepository bookReadonlyRepository,
                           AuthorRepository authorRepository, GenreRepository genreRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.bookReadonlyRepository = bookReadonlyRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void addBook(AddBookCommand command) {
        Book book = Book.builder()
                .id(BookId.of(UUID.randomUUID().toString()))
                .title(command.getTitle())
                .description(command.getDescription())
                .author(getAuthorById(command.getAuthorId()))
                .genre(getGenreById(command.getGenreId()))
                .publisher(getPublisherById(command.getPublisherId()))
                .publicationDate(Instant.parse(command.getPublicationDate()))
                .build();

        bookRepository.save(book);
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.find(AuthorId.of(authorId)).orElseThrow(() -> newAuthorNotFoundException(authorId));
    }

    private Genre getGenreById(String genreId) {
        return genreRepository.find(GenreId.of(genreId)).orElseThrow(() -> newGenreNotFoundException(genreId));
    }

    private Publisher getPublisherById(String publisherId) {
        return publisherRepository.find(PublisherId.of(publisherId)).orElseThrow(() -> newPublisherNotFoundException(publisherId));
    }

    @Override
    public BookData getBookById(String bookId) {
        return bookReadonlyRepository.findById(bookId)
                .orElseThrow(() -> newBookNotFoundException(bookId));
    }

    @Override
    public Paginated<BookData> getAllBooksByQuery(BookQuery query, Pagination pagination) {
        return bookReadonlyRepository.findAllByQuery(query, pagination);
    }

    @Override
    public void updateBook(UpdateBookCommand command) {
        Book book = bookRepository.find(BookId.of(command.getId()))
                .orElseThrow(() -> newBookNotFoundException(command.getId()));

        book = book.withTitle(command.getTitle());
        book = book.withDescription(command.getDescription());
        book = book.withPublicationDate(Instant.parse(command.getPublicationDate()));

        Author author = authorRepository.find(AuthorId.of(command.getAuthorId()))
                .orElseThrow(() -> newAuthorNotFoundException(command.getAuthorId()));
        book = book.withAuthor(author);

        Genre genre = genreRepository.find(GenreId.of(command.getGenreId()))
                .orElseThrow(() -> newGenreNotFoundException(command.getGenreId()));
        book = book.withGenre(genre);

        Publisher publisher = publisherRepository.find(PublisherId.of(command.getPublisherId()))
                .orElseThrow(() -> newPublisherNotFoundException(command.getPublisherId()));
        book = book.withPublisher(publisher);

        bookRepository.update(book);
    }

    @Override
    public void removeBook(String bookId) {
        Book book = bookRepository.find(BookId.of(bookId))
                .orElseThrow(() -> newBookNotFoundException(bookId));
        bookRepository.delete(book);
    }

    private NotFoundException newBookNotFoundException(String bookId) {
        return newNotFoundException("Book", bookId);
    }

    private NotFoundException newAuthorNotFoundException(String authorId) {
        return newNotFoundException("Author", authorId);
    }

    private NotFoundException newGenreNotFoundException(String genreId) {
        return newNotFoundException("Genre", genreId);
    }

    private NotFoundException newPublisherNotFoundException(String publisherId) {
        return newNotFoundException("Publisher", publisherId);
    }

    private NotFoundException newNotFoundException(String subject, String id) {
        return new NotFoundException(subject + " not found: " + id);
    }
}
