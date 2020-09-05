package pl.zmudzin.library.core.application.catalog.genre;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.Optional;

public interface GenreReadonlyRepository {

    Optional<GenreData> findById(String genreId);

    Paginated<GenreData> findAllByQuery(GenreQuery query, Pagination pagination);
}
