package pl.zmudzin.library.ui.catalog.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.zmudzin.library.application.catalog.book.BookCreateRequest;
import pl.zmudzin.library.application.catalog.book.BookUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.*;
import pl.zmudzin.library.util.*;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void createBook_nullTitle_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setTitle(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullAuthorId_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setAuthorId(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullGenreId_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setGenreId(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullPublisherId_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setPublisherId(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullDescription_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setDescription(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullPublicationDate_400() throws Exception {
        BookCreateRequest request = getBookCreateRequest();
        request.setPublicationDate(null);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_notAuthenticated_401() throws Exception {
        BookCreateRequest request = getBookCreateRequest();

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createBook_notAuthorized_403() throws Exception {
        BookCreateRequest request = getBookCreateRequest();

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    private static BookCreateRequest getBookCreateRequest() {
        return BookTestUtil.getBookCreateRequest(1L, 2L, 3L);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void createBook_validArguments_200() throws Exception {
        Author author = AuthorTestUtil.createAuthor(authorRepository);
        Genre genre = GenreTestUtil.createGenre(genreRepository);
        Publisher publisher = PublisherTestUtil.createPublisher(publisherRepository);

        BookCreateRequest request = BookTestUtil.getBookCreateRequest(author.getId(), genre.getId(), publisher.getId());

        ResultActions resultActions = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void getBookById_nonExistingBook_404() throws Exception {
        mockMvc.perform(get("/books/" + 1000))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookById_existingBook_200() throws Exception {
        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        ResultActions resultActions = mockMvc.perform(get("/books/" + book.getId()));

        validate(book, resultActions);
    }

    @Test
    void updateBook_notAuthenticated_401() throws Exception {
        mockMvc.perform(put("/books/" + 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BookUpdateRequest())))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateBook_notAuthorized_403() throws Exception {
        mockMvc.perform(put("/books/" + 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BookUpdateRequest())))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateBook_nonExistingBook_404() throws Exception {
        mockMvc.perform(put("/books/" + 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BookUpdateRequest())))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateBook_validArguments_200() throws Exception {
        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        Author author = AuthorTestUtil.createAuthor(authorRepository);
        Genre genre = GenreTestUtil.createGenre(genreRepository);
        Publisher publisher = PublisherTestUtil.createPublisher(publisherRepository);

        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle(book.getTitle() + "Updated");
        request.setAuthorId(author.getId());
        request.setGenreId(genre.getId());
        request.setPublisherId(publisher.getId());
        request.setDescription(book.getDescription() + "Updated");
        request.setPublicationDate(book.getPublicationDate().plusDays(1));

        ResultActions resultActions = mockMvc.perform(put("/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        validate(request, resultActions);
    }

    @Test
    void deleteBook_notAuthenticated_401() throws Exception {
        mockMvc.perform(delete("/books/" + 1000))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteBook_notAuthorized_403() throws Exception {
        mockMvc.perform(delete("/books/" + 1000))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteBook_notExistingBook_404() throws Exception {
        mockMvc.perform(delete("/books/" + 1000))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteBook_validArguments_200() throws Exception {
        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        mockMvc.perform(delete("/books/" + book.getId()))
                .andExpect(status().isNoContent());
    }

    private static void validate(BookCreateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getTitle(), request.getAuthorId(), request.getGenreId(), request.getPublisherId(),
                request.getDescription(), request.getPublicationDate(), resultActions);
    }

    private static void validate(BookUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getTitle(), request.getAuthorId(), request.getGenreId(), request.getPublisherId(),
                request.getDescription(), request.getPublicationDate(), resultActions);
    }

    private static void validate(Book book, ResultActions resultActions) throws Exception {
        validate(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId(), book.getPublisher().getId(),
                book.getDescription(), book.getPublicationDate(), resultActions);
    }

    private static void validate(String title, Long authorId, Long genreId, Long publisherId, String description,
                                 LocalDate publicationDate, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.author.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.genre.id", is(genreId.intValue())))
                .andExpect(jsonPath("$.publisher.id", is(publisherId.intValue())))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.publicationDate", is(publicationDate.toString())));
    }
}