package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.catalog.genre.GenreCreateRequest;
import pl.zmudzin.library.application.catalog.genre.GenreUpdateRequest;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.GenreRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class GenreTestUtil {

    public static final Long ID = 1L;
    public static final String NAME = "name";

    public static GenreCreateRequest getGenreCreateRequest() {
        return getGenreCreateRequest(NAME);
    }

    public static GenreCreateRequest getGenreCreateRequest(String name) {
        GenreCreateRequest request = new GenreCreateRequest();
        request.setName(name);
        return request;
    }

    public static GenreUpdateRequest getGenreUpdateRequest() {
        return getGenreUpdateRequest(NAME + "Updated");
    }

    public static GenreUpdateRequest getGenreUpdateRequest(String name) {
        GenreUpdateRequest request = new GenreUpdateRequest();
        request.setName(name);
        return request;
    }

    public static Genre createGenre(GenreRepository genreRepository) {
        return createGenre(genreRepository, NAME);
    }

    public static Genre createGenre(GenreRepository genreRepository, String name) {
        Genre genre = new Genre(name);
        return genreRepository.save(genre);
    }

    public static Genre mockGenre() {
        return mockGenre(ID, NAME);
    }

    public static Genre mockGenre(Long id, String name) {
        Genre genre = Mockito.mock(Genre.class, withSettings().lenient());
        when(genre.getId()).thenReturn(id);
        when(genre.getName()).thenReturn(name);
        return genre;
    }
}
