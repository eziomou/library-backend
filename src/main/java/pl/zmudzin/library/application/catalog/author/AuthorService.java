package pl.zmudzin.library.application.catalog.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface AuthorService {

    AuthorData createAuthor(AuthorCreateRequest request);

    AuthorData getAuthorById(Long id);

    Page<AuthorData> getAllAuthors(AuthorSearchRequest request, Pageable pageable);

    AuthorData updateAuthorById(Long id, AuthorUpdateRequest request);

    void deleteAuthorById(Long id);
}
