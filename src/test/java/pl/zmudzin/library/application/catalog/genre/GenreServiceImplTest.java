package pl.zmudzin.library.application.catalog.genre;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.GenreRepository;
import pl.zmudzin.library.util.GenreTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void createGenre_validArguments_createsGenre() {
        GenreCreateRequest request = GenreTestUtil.getGenreCreateRequest();
        Genre genre = GenreTestUtil.mockGenre();

        when(genreRepository.save(any())).thenReturn(genre);

        GenreData data = genreService.createGenre(request);

        verify(genreRepository, times(1)).save(any());

        assertGenreEquals(genre, data);
    }

    @Test
    void updateGenreById_validArguments_updatesGenre() {
        GenreUpdateRequest request = GenreTestUtil.getGenreUpdateRequest();
        Genre genre = GenreTestUtil.mockGenre();
        Genre updatedGenre = GenreTestUtil.mockGenre(1L, request.getName());

        when(genre.getId()).thenReturn(1L);
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        when(genreRepository.save(any())).thenReturn(updatedGenre);

        GenreData data = genreService.updateGenreById(genre.getId(), request);

        verify(genre, times(1)).setName(request.getName());
        verify(genreRepository, times(1)).save(any());

        assertGenreEquals(updatedGenre, data);
    }

    @Test
    void deleteGenreById_existingGenre_deletesGenre() {
        Genre genre = GenreTestUtil.mockGenre();

        when(genre.getId()).thenReturn(1L);
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));

        genreService.deleteGenreById(genre.getId());

        verify(genreRepository, times(1)).delete(genre);
    }

    private static void assertGenreEquals(Genre genre, GenreData data) {
        assertEquals(genre.getName(), data.getName());
    }
}