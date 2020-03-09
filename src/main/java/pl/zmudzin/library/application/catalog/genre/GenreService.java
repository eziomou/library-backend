package pl.zmudzin.library.application.catalog.genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface GenreService {

    GenreData createGenre(GenreCreateRequest request);

    GenreData getGenreById(Long id);

    Page<GenreData> getAllGenres(GenreSearchRequest request, Pageable pageable);

    GenreData updateGenreById(Long id, GenreUpdateRequest request);

    void deleteGenreById(Long id);
}
