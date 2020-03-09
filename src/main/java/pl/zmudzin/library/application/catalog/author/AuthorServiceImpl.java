package pl.zmudzin.library.application.catalog.author;

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
import pl.zmudzin.library.application.util.PersonPredicateBuilder;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.AuthorRepository;

import javax.persistence.criteria.Path;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"authors", "books"}, allEntries = true)
    @Override
    public AuthorData createAuthor(AuthorCreateRequest request) {
        Author author = new Author(request.getFirstName(), request.getLastName());
        author = authorRepository.save(author);
        return map(author);
    }

    @Cacheable("authors")
    @Override
    public AuthorData getAuthorById(Long id) {
        Author author = getAuthorEntityById(id);
        return map(author);
    }

    @Cacheable("authors")
    @Override
    public Page<AuthorData> getAllAuthors(AuthorSearchRequest request, Pageable pageable) {
        Specification<Author> specification = (r, cq, cb) -> PersonPredicateBuilder.builder(r, cb)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFullName())
                .build();

        return authorRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"authors", "books"}, allEntries = true)
    @Override
    public AuthorData updateAuthorById(Long id, AuthorUpdateRequest request) {
        Author author = getAuthorEntityById(id);

        if (request.getFirstName() != null) {
            author.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            author.setLastName(request.getLastName());
        }
        author = authorRepository.save(author);
        return map(author);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"authors", "books"}, allEntries = true)
    @Override
    public void deleteAuthorById(Long id) {
        Author author = getAuthorEntityById(id);

        if (!author.getBooks().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The author has books");
        }
        authorRepository.delete(author);
    }

    private Author getAuthorEntityById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AuthorData map(Author author) {
        AuthorData data = new AuthorData();
        data.setId(author.getId());
        data.setFirstName(author.getFirstName());
        data.setLastName(author.getLastName());
        return data;
    }
}
