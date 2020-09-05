package pl.zmudzin.library.core.application.catalog.author;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.Optional;

public interface AuthorReadonlyRepository {

    Optional<AuthorData> findById(String authorId);

    Paginated<AuthorData> findAllByQuery(AuthorQuery query, Pagination pagination);
}
