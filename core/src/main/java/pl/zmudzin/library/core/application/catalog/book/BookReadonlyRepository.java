package pl.zmudzin.library.core.application.catalog.book;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.Optional;

public interface BookReadonlyRepository {

    Optional<BookData> findById(String bookId);

    Paginated<BookData> findAllByQuery(BookQuery query, Pagination pagination);
}
