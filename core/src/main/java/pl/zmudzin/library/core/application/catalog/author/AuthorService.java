package pl.zmudzin.library.core.application.catalog.author;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface AuthorService {

    void addAuthor(String firstName, String lastName);

    AuthorData getAuthorById(String authorId);

    Paginated<AuthorData> getAllAuthorsByQuery(AuthorQuery query, Pagination pagination);

    void updateAuthor(String authorId, String firstName, String lastName);

    void removeAuthor(String authorId);
}
