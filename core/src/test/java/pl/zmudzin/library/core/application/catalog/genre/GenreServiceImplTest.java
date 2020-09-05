package pl.zmudzin.library.core.application.catalog.genre;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreReadonlyRepository genreReadonlyRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void addGenre_validGenre_addsGenre() {
        genreService.addGenre("Foo");

        verify(genreRepository, times(1)).save(any());
    }

    @Test
    void updateGenre_existingGenre_updatesGenre() {
        Genre genre = new Genre(GenreId.of(""), "Foo");

        when(genreRepository.find(genre.getId())).thenReturn(Optional.of(genre));

        genreService.updateGenre(genre.getId().toString(), "Bar");

        verify(genreRepository, times(1)).update(any());
    }

    @Test
    void updateGenre_notExistingGenre_throwsException() {
        GenreId genreId = GenreId.of("");

        when(genreRepository.find(genreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.updateGenre(genreId.toString(), "Foo"));

        verify(genreRepository, times(0)).update(any());
    }

    @Test
    void removeGenre_existingGenre_deletesGenre() {
        Genre genre = new Genre(GenreId.of(""), "Foo");

        when(genreRepository.find(genre.getId())).thenReturn(Optional.of(genre));

        genreService.removeGenre(genre.getId().toString());

        verify(genreRepository, times(1)).delete(any());
    }

    @Test
    void removeGenre_notExistingGenre_throwsException() {
        GenreId genreId = GenreId.of("");

        when(genreRepository.find(genreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.removeGenre(genreId.toString()));

        verify(genreRepository, times(0)).delete(any());
    }
}