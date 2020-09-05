package pl.zmudzin.library.core.application.catalog.author;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.UUID;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorReadonlyRepository authorReadonlyRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorReadonlyRepository authorReadonlyRepository) {
        this.authorRepository = authorRepository;
        this.authorReadonlyRepository = authorReadonlyRepository;
    }

    @Override
    public void addAuthor(String firstName, String lastName) {
        Author author = createAuthor(firstName, lastName);
        authorRepository.save(author);
    }

    private Author createAuthor(String firstName, String lastName) {
        AuthorId authorId = new AuthorId(UUID.randomUUID().toString());
        return new Author(authorId, firstName, lastName);
    }

    @Override
    public AuthorData getAuthorById(String authorId) {
        return authorReadonlyRepository.findById(authorId).orElseThrow(() -> newNotFoundException(authorId));
    }

    @Override
    public Paginated<AuthorData> getAllAuthorsByQuery(AuthorQuery query, Pagination pagination) {
        return authorReadonlyRepository.findAllByQuery(query, pagination);
    }

    @Override
    public void updateAuthor(String authorId, String firstName, String lastName) {
        Author author = authorRepository.find(AuthorId.of(authorId))
                .orElseThrow(() -> newNotFoundException(authorId));

        author = author.withFirstName(firstName);
        author = author.withLastName(lastName);
        authorRepository.update(author);
    }

    @Override
    public void removeAuthor(String authorId) {
        Author author = authorRepository.find(AuthorId.of(authorId))
                .orElseThrow(() -> newNotFoundException(authorId));
        authorRepository.delete(author);
    }

    private NotFoundException newNotFoundException(String authorId) {
        return new NotFoundException("Author not found: " + authorId);
    }
}
