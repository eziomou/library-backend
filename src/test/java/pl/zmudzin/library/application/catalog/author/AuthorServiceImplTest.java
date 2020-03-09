package pl.zmudzin.library.application.catalog.author;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.AuthorRepository;
import pl.zmudzin.library.util.AuthorTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void createAuthor_validArguments_createsAuthor() {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();
        Author author = AuthorTestUtil.mockAuthor();

        when(authorRepository.save(any())).thenReturn(author);

        AuthorData data = authorService.createAuthor(request);

        assertAuthorEquals(author, data);
    }

    @Test
    void updateAuthorById_validArguments_updatesAuthor() {
        AuthorUpdateRequest request = AuthorTestUtil.getAuthorUpdateRequest();
        Author author = AuthorTestUtil.mockAuthor();
        Author updatedAuthor = AuthorTestUtil.mockAuthor(1L, request.getFirstName(), request.getLastName());

        when(author.getId()).thenReturn(1L);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(authorRepository.save(any())).thenReturn(updatedAuthor);

        AuthorData data = authorService.updateAuthorById(author.getId(), request);

        verify(author, times(1)).setFirstName(request.getFirstName());
        verify(author, times(1)).setLastName(request.getLastName());
        verify(authorRepository, times(1)).save(any());

        assertAuthorEquals(updatedAuthor, data);
    }

    @Test
    void deleteAuthorById_existingAuthor_deletesAuthor() {
        Author author = AuthorTestUtil.mockAuthor();

        when(author.getId()).thenReturn(1L);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(author.getId());

        verify(authorRepository, times(1)).delete(author);
    }

    private static void assertAuthorEquals(Author author, AuthorData data) {
        assertAll(
                () -> assertEquals(author.getFirstName(), data.getFirstName()),
                () -> assertEquals(author.getLastName(), data.getLastName())
        );
    }
}