package pl.zmudzin.library.core.application.catalog.genre;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface GenreService {

    void addGenre(String name);

    GenreData getGenreById(String genreId);

    Paginated<GenreData> getAllGenresByQuery(GenreQuery query, Pagination pagination);

    void updateGenre(String genreId, String name);

    void removeGenre(String genreId);
}
