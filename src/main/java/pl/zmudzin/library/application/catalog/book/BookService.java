package pl.zmudzin.library.application.catalog.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface BookService {

    BookData createBook(BookCreateRequest request);

    BookData getBookById(Long id);

    Page<BookData> getAllBooks(BookSearchRequest request, Pageable pageable);

    BookData updateBookById(Long id, BookUpdateRequest request);

    void deleteBookById(Long id);
}
