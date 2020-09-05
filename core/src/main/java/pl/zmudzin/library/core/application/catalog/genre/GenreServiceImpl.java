package pl.zmudzin.library.core.application.catalog.genre;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.UUID;

public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreReadonlyRepository genreReadonlyRepository;

    public GenreServiceImpl(GenreRepository genreRepository, GenreReadonlyRepository genreReadonlyRepository) {
        this.genreRepository = genreRepository;
        this.genreReadonlyRepository = genreReadonlyRepository;
    }

    @Override
    public void addGenre(String name) {
        Genre genre = createGenre(name);
        genreRepository.save(genre);
    }

    private Genre createGenre(String name) {
        GenreId genreId = new GenreId(UUID.randomUUID().toString());
        return new Genre(genreId, name);
    }

    @Override
    public GenreData getGenreById(String genreId) {
        return genreReadonlyRepository.findById(genreId).orElseThrow(() -> newGenreNotFoundException(genreId));
    }

    @Override
    public Paginated<GenreData> getAllGenresByQuery(GenreQuery query, Pagination pagination) {
        return genreReadonlyRepository.findAllByQuery(query, pagination);
    }

    @Override
    public void updateGenre(String genreId, String name) {
        Genre genre = genreRepository.find(GenreId.of(genreId))
                .orElseThrow(() -> newGenreNotFoundException(genreId));

        genre = genre.withName(name);
        genreRepository.update(genre);
    }

    @Override
    public void removeGenre(String genreId) {
        Genre genre = genreRepository.find(GenreId.of(genreId))
                .orElseThrow(() -> newGenreNotFoundException(genreId));
        genreRepository.delete(genre);
    }

    private NotFoundException newGenreNotFoundException(String genreId) {
        return new NotFoundException("Genre not found: "+ genreId);
    }
}
