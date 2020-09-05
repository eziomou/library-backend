package pl.zmudzin.library.core.application.catalog.book;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface BookService {

    void addBook(AddBookCommand command);

    BookData getBookById(String bookId);

    Paginated<BookData> getAllBooksByQuery(BookQuery query, Pagination pagination);

    void updateBook(UpdateBookCommand command);

    void removeBook(String bookId);
}
