package pl.zmudzin.library.spring.catalog.genre;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.catalog.genre.GenreData;
import pl.zmudzin.library.core.application.catalog.genre.GenreService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Secured({Role.LIBRARIAN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addGenre(@Valid @RequestBody AddGenreRequest request) {
        genreService.addGenre(request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenreData> getGenreById(@PathVariable String genreId) {
        GenreData genre = genreService.getGenreById(genreId);
        return ResponseEntity.ok(genre);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Paginated<GenreData>> getAllGenres(@Valid RestGenreQuery query, @Valid RestPagination pagination) {
        Paginated<GenreData> genres = genreService.getAllGenresByQuery(query, pagination);
        return ResponseEntity.ok(genres);
    }

    @Secured({Role.LIBRARIAN})
    @PutMapping(path = "/{genreId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGenre(@PathVariable String genreId, @Valid @RequestBody UpdateGenreRequest request) {
        genreService.updateGenre(genreId, request.getName());
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @DeleteMapping(path = "/{genreId}")
    public ResponseEntity<GenreData> removeGenre(@PathVariable String genreId) {
        genreService.removeGenre(genreId);
        return ResponseEntity.noContent().build();
    }
}
