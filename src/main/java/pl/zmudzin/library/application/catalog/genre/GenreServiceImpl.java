package pl.zmudzin.library.application.catalog.genre;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.GenreRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"genres", "books"}, allEntries = true)
    @Override
    public GenreData createGenre(GenreCreateRequest request) {
        Genre genre = new Genre(request.getName());
        genre = genreRepository.save(genre);
        return map(genre);
    }

    @Cacheable("genres")
    @Override
    public GenreData getGenreById(Long id) {
        Genre genre = getGenreEntityById(id);
        return map(genre);
    }

    @Cacheable("genres")
    @Override
    public Page<GenreData> getAllGenres(GenreSearchRequest request, Pageable pageable) {
        Specification<Genre> specification = (r, cq, cb) -> PredicateBuilder.builder(cb)
                .like(r.get("name"), request.getName())
                .build();

        return genreRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"genres", "books"}, allEntries = true)
    @Override
    public GenreData updateGenreById(Long id, GenreUpdateRequest request) {
        Genre genre = getGenreEntityById(id);

        if (request.getName() != null) {
            genre.setName(request.getName());
        }
        genre = genreRepository.save(genre);
        return map(genre);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"genres", "books"}, allEntries = true)
    @Override
    public void deleteGenreById(Long id) {
        Genre genre = getGenreEntityById(id);

        if (!genre.getBooks().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The genre has books");
        }
        genreRepository.delete(genre);
    }

    private Genre getGenreEntityById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private GenreData map(Genre genre) {
        GenreData data = new GenreData();
        data.setId(genre.getId());
        data.setName(genre.getName());
        return data;
    }
}
