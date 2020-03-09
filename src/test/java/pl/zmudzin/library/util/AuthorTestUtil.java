package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.catalog.author.AuthorCreateRequest;
import pl.zmudzin.library.application.catalog.author.AuthorUpdateRequest;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.AuthorRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class AuthorTestUtil {

    public static final Long ID = 1L;
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public static AuthorCreateRequest getAuthorCreateRequest() {
        return getAuthorCreateRequest(FIRST_NAME, LAST_NAME);
    }

    public static AuthorCreateRequest getAuthorCreateRequest(String firstName, String lastName) {
        AuthorCreateRequest request = new AuthorCreateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        return request;
    }

    public static AuthorUpdateRequest getAuthorUpdateRequest() {
        return getAuthorUpdateRequest(FIRST_NAME + "Updated", LAST_NAME + "Updated");
    }

    public static AuthorUpdateRequest getAuthorUpdateRequest(String firstName, String lastName) {
        AuthorUpdateRequest request = new AuthorUpdateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        return request;
    }

    public static Author createAuthor(AuthorRepository authorRepository) {
        return createAuthor(authorRepository, FIRST_NAME, LAST_NAME);
    }

    public static Author createAuthor(AuthorRepository authorRepository, String firstName, String lastName) {
        Author author = new Author(firstName, lastName);
        return authorRepository.save(author);
    }

    public static Author mockAuthor(){
        return mockAuthor(ID, FIRST_NAME, LAST_NAME);
    }

    public static Author mockAuthor(Long id, String firstName, String lastName) {
        Author author = Mockito.mock(Author.class, withSettings().lenient());
        when(author.getId()).thenReturn(id);
        when(author.getFirstName()).thenReturn(firstName);
        when(author.getLastName()).thenReturn(lastName);
        return author;
    }
}
