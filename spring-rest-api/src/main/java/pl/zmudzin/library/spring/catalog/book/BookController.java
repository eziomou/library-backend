package pl.zmudzin.library.spring.catalog.book;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.catalog.book.AddBookCommand;
import pl.zmudzin.library.core.application.catalog.book.BookData;
import pl.zmudzin.library.core.application.catalog.book.BookService;
import pl.zmudzin.library.core.application.catalog.book.UpdateBookCommand;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping(path = BookController.BASE_PATH)
public class BookController {

    public static final String BASE_PATH = "/books";

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBook(@Valid @RequestBody AddBookRequest request) {
        bookService.addBook(AddBookCommand.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .publicationDate(request.getPublicationDate())
                .authorId(request.getAuthorId())
                .genreId(request.getGenreId())
                .publisherId(request.getPublisherId())
                .build());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookData> getBook(@PathVariable String bookId) {
        BookData book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<Paginated<BookData>> getAllBooks(@Valid RestBookQuery query, @Valid RestPagination pagination) {
        Paginated<BookData> books = bookService.getAllBooksByQuery(query, pagination);
        return ResponseEntity.ok(books);
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @PutMapping(path = "/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(@PathVariable String bookId, UpdateBookRequest request) {
        bookService.updateBook(UpdateBookCommand.builder()
                .id(bookId)
                .title(request.getTitle())
                .description(request.getDescription())
                .publicationDate(request.getPublicationDate())
                .authorId(request.getAuthorId())
                .genreId(request.getGenreId())
                .publisherId(request.getPublisherId())
                .build());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
        bookService.removeBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
