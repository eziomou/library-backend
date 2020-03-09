package pl.zmudzin.library.ui.catalog.genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.catalog.genre.*;
import pl.zmudzin.library.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/genres", produces = "application/json")
public class GenreController {

    private GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<GenreData>> createGenre(@Valid @RequestBody GenreCreateRequest request) {
        GenreData data = genreService.createGenre(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GenreData>> getGenreById(@PathVariable Long id) {
        GenreData data = genreService.getGenreById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GenreData>>> getAllGenres(@Valid GenreSearchRequest request,
                                                                 @PageableDefault Pageable pageable) {
        Page<GenreData> data = genreService.getAllGenres(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<GenreData>> updateGenreById(@PathVariable Long id,
                                                        @Valid @RequestBody GenreUpdateRequest request) {
        GenreData data = genreService.updateGenreById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<GenreData>> deleteGenreById(@PathVariable Long id) {
        genreService.deleteGenreById(id);
        return ResponseEntity.noContent().build();
    }
}
