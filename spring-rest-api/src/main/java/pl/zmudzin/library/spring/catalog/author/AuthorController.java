package pl.zmudzin.library.spring.catalog.author;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.catalog.author.AuthorData;
import pl.zmudzin.library.core.application.catalog.author.AuthorService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Secured({Role.LIBRARIAN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAuthor(@Valid @RequestBody AddAuthorRequest request) {
        authorService.addAuthor(request.getFirstName(), request.getLastName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorData> getAuthorById(@PathVariable String authorId) {
        AuthorData author = authorService.getAuthorById(authorId);
        return ResponseEntity.ok(author);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Paginated<AuthorData>> getAllAuthorsByQuery(@Valid RestAuthorQuery query, @Valid RestPagination pagination) {
        Paginated<AuthorData> authors = authorService.getAllAuthorsByQuery(query, pagination);
        return ResponseEntity.ok(authors);
    }

    @Secured({Role.LIBRARIAN})
    @PutMapping(path = "/{authorId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(@PathVariable String authorId, @Valid @RequestBody UpdateAuthorRequest request) {
        authorService.updateAuthor(authorId, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<AuthorData> removeAuthor(@PathVariable String authorId) {
        authorService.removeAuthor(authorId);
        return ResponseEntity.noContent().build();
    }
}
