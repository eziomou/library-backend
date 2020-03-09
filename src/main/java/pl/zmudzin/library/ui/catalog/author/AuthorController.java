package pl.zmudzin.library.ui.catalog.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.catalog.author.*;
import pl.zmudzin.library.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/authors", produces = "application/json")
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<AuthorData>> createAuthor(@Valid @RequestBody AuthorCreateRequest request) {
        AuthorData data = authorService.createAuthor(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AuthorData>> getAuthorById(@PathVariable Long id) {
        AuthorData data = authorService.getAuthorById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AuthorData>>> getAllAuthors(@Valid AuthorSearchRequest request,
                                                                             @PageableDefault Pageable pageable) {
        Page<AuthorData> data = authorService.getAllAuthors(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<AuthorData>> updateAuthorById(@PathVariable Long id,
                                                        @Valid @RequestBody AuthorUpdateRequest request) {
        AuthorData data = authorService.updateAuthorById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<AuthorData>> deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }
}
