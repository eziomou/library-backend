package pl.zmudzin.library.ui.catalog.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.catalog.book.*;
import pl.zmudzin.library.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/books", produces = "application/json")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<BookData>> createBook(@Valid @RequestBody BookCreateRequest request) {
        BookData data = bookService.createBook(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        BookData data = bookService.getBookById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BookData>>> getAllBooks(@Valid BookSearchRequest request,
                                                                 @PageableDefault Pageable pageable) {
        Page<BookData> data = bookService.getAllBooks(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<BookData>> updateBookById(@PathVariable Long id,
                                                        @Valid @RequestBody BookUpdateRequest request) {
        BookData data = bookService.updateBookById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
