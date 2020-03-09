package pl.zmudzin.library.application.catalog.book;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.catalog.author.AuthorBasicData;
import pl.zmudzin.library.application.catalog.genre.GenreBasicData;
import pl.zmudzin.library.application.catalog.publisher.PublisherBasicData;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.*;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private PublisherRepository publisherRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = "books", allEntries = true)
    @Override
    public BookData createBook(BookCreateRequest request) {
        Author author = getAuthorById(request.getAuthorId());
        Genre genre = getGenreById(request.getGenreId());
        Publisher publisher = getPublisherById(request.getPublisherId());

        Book book = new Book(
                request.getTitle(),
                author,
                genre,
                publisher,
                request.getDescription(),
                request.getPublicationDate()
        );
        book = bookRepository.save(book);
        return map(book);
    }

    private Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    private Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
    }

    private Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));
    }

    @Cacheable("books")
    @Override
    public BookData getBookById(Long id) {
        Book book = getBookEntityById(id);
        return map(book);
    }

    @Cacheable("books")
    @Override
    public Page<BookData> getAllBooks(BookSearchRequest request, Pageable pageable) {
        Specification<Book> specification = (r, cq, cb) -> BookPredicateBuilder.builder(r, cb)
                        .title(request.getTitle())
                        .authorId(request.getAuthorId())
                        .authorFullName(request.getAuthorFullName())
                        .genreId(request.getGenreId())
                        .genreName(request.getGenreName())
                        .publisherId(request.getPublisherId())
                        .publisherName(request.getPublisherName())
                        .build();

        return bookRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = "books", allEntries = true)
    @Override
    public BookData updateBookById(Long id, BookUpdateRequest request) {
        Book book = getBookEntityById(id);

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthorId() != null) {
            if (!book.getAuthor().getId().equals(request.getAuthorId())) {
                Author author = getAuthorById(request.getAuthorId());
                book.changeAuthor(author);
            }
        }
        if (request.getGenreId() != null) {
            if (!book.getGenre().getId().equals(request.getGenreId())) {
                Genre genre = getGenreById(request.getGenreId());
                book.changeGenre(genre);
            }
        }
        if (request.getPublisherId() != null) {
            if (!book.getPublisher().getId().equals(request.getPublisherId())) {
                Publisher publisher = getPublisherById(request.getPublisherId());
                book.changePublisher(publisher);
            }
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getPublicationDate() != null) {
            book.setPublicationDate(request.getPublicationDate());
        }
        book = bookRepository.save(book);
        return map(book);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = "books", allEntries = true)
    @Override
    public void deleteBookById(Long id) {
        Book book = getBookEntityById(id);
        bookRepository.delete(book);
    }

    private Book getBookEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private BookData map(Book book) {
        BookData data = new BookData();
        data.setId(book.getId());
        data.setTitle(book.getTitle());
        data.setAuthor(new AuthorBasicData(
                book.getAuthor().getId(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName()
        ));
        data.setGenre(new GenreBasicData(
                book.getGenre().getId(),
                book.getGenre().getName()
        ));
        data.setPublisher(new PublisherBasicData(
                book.getPublisher().getId(),
                book.getPublisher().getName()
        ));
        data.setDescription(book.getDescription());
        data.setPublicationDate(book.getPublicationDate());
        data.setLoaned(book.isLoaned());
        data.setAverageRating(book.getAverageRating());
        return data;
    }
}
