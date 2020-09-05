package pl.zmudzin.library.core.application.catalog.author;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorReadonlyRepository authorReadonlyRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void addAuthor_validAuthor_addsAuthor() {
        authorService.addAuthor("Foo", "Bar");

        verify(authorRepository, times(1)).save(any());
    }

    @Test
    void updateAuthor_existingAuthor_updatesAuthor() {
        Author author = new Author(AuthorId.of(""), "Foo", "Bar");

        when(authorRepository.find(author.getId())).thenReturn(Optional.of(author));

        authorService.updateAuthor(author.getId().toString(), "Baz", "Qux");

        verify(authorRepository, times(1)).update(any());
    }

    @Test
    void updateAuthor_notExistingAuthor_throwsException() {
        AuthorId authorId = AuthorId.of("");

        when(authorRepository.find(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.updateAuthor(authorId.toString(), "Foo", "Bar"));

        verify(authorRepository, times(0)).update(any());
    }

    @Test
    void removeAuthor_existingAuthor_deletesAuthor() {
        Author author = new Author(AuthorId.of(""), "Foo", "Bar");

        when(authorRepository.find(author.getId())).thenReturn(Optional.of(author));

        authorService.removeAuthor(author.getId().toString());

        verify(authorRepository, times(1)).delete(any());
    }

    @Test
    void removeAuthor_notExistingAuthor_throwsException() {
        AuthorId authorId = AuthorId.of("");

        when(authorRepository.find(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.removeAuthor(authorId.toString()));

        verify(authorRepository, times(0)).delete(any());
    }
}