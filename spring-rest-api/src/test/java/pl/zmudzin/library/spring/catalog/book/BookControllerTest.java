package pl.zmudzin.library.spring.catalog.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.catalog.book.BookData;
import pl.zmudzin.library.core.application.catalog.book.BookQuery;
import pl.zmudzin.library.core.application.catalog.book.BookService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BookControllerTest {

    private static final Book BOOK = Book.builder()
            .id(BookId.of("1"))
            .title("Foo")
            .description("Bar")
            .publicationDate(Instant.now())
            .author(new Author(AuthorId.of("1"), "Foo", "Bar"))
            .genre(new Genre(GenreId.of("1"), "Foo"))
            .publisher(new Publisher(PublisherId.of("1"), "Foo"))
            .build();

    private static final BookData BOOK_DATA = BookData.builder()
            .id(BOOK.getId().toString())
            .title(BOOK.getTitle())
            .description(BOOK.getDescription())
            .publicationDate(BOOK.getPublicationDate().toString())
            .author(builder -> builder
                    .id(BOOK.getAuthor().getId().toString())
                    .firstName(BOOK.getAuthor().getFirstName())
                    .lastName(BOOK.getAuthor().getLastName()))
            .genre(builder -> builder
                    .id(BOOK.getGenre().getId().toString())
                    .name(BOOK.getGenre().getName()))
            .publisher(builder -> builder
                    .id(BOOK.getPublisher().getId().toString())
                    .name(BOOK.getPublisher().getName()))
            .build();

    private static final AddBookRequest ADD_BOOK_REQUEST = AddBookRequest.builder()
            .title(BOOK_DATA.getTitle())
            .description(BOOK_DATA.getDescription())
            .publicationDate(BOOK_DATA.getPublicationDate())
            .authorId(BOOK_DATA.getAuthor().getId())
            .genreId(BOOK_DATA.getPublisher().getId())
            .publisherId(BOOK_DATA.getPublisher().getId())
            .build();

    private static final UpdateBookRequest UPDATE_BOOK_REQUEST = UpdateBookRequest.builder()
            .title(BOOK_DATA.getTitle())
            .description(BOOK_DATA.getDescription())
            .publicationDate(BOOK_DATA.getPublicationDate())
            .authorId(BOOK_DATA.getAuthor().getId())
            .genreId(BOOK_DATA.getPublisher().getId())
            .publisherId(BOOK_DATA.getPublisher().getId())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void addBook_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ADD_BOOK_REQUEST)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void addBook_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ADD_BOOK_REQUEST)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void addBook_asLibrarian_returnsOk() throws Exception {
        doNothing().when(bookService).addBook(any());

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ADD_BOOK_REQUEST)))
                .andExpect(status().isOk());
    }

    @Test
    void getBookById_existingBook_returnsOk() throws Exception {
        when(bookService.getBookById(BOOK.getId().toString())).thenReturn(BOOK_DATA);

        mockMvc.perform(get("/books/" + BOOK.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(BOOK_DATA.getTitle())))
                .andExpect(jsonPath("$.description", is(BOOK_DATA.getDescription())))
                .andExpect(jsonPath("$.publicationDate", is(BOOK_DATA.getPublicationDate())))
                .andExpect(jsonPath("$.author.id", is(BOOK_DATA.getAuthor().getId())))
                .andExpect(jsonPath("$.author.firstName", is(BOOK_DATA.getAuthor().getFirstName())))
                .andExpect(jsonPath("$.author.lastName", is(BOOK_DATA.getAuthor().getLastName())))
                .andExpect(jsonPath("$.author.fullName", is(BOOK_DATA.getAuthor().getFullName())))
                .andExpect(jsonPath("$.genre.id", is(BOOK_DATA.getGenre().getId())))
                .andExpect(jsonPath("$.genre.name", is(BOOK_DATA.getGenre().getName())))
                .andExpect(jsonPath("$.publisher.id", is(BOOK_DATA.getPublisher().getId())))
                .andExpect(jsonPath("$.publisher.name", is(BOOK_DATA.getPublisher().getName())));
    }

    @Test
    void getAllBooksByQuery_validRequest_returnsOk() throws Exception {
        BookQuery query = new RestBookQuery();
        Pagination pagination = new RestPagination();

        when(bookService.getAllBooksByQuery(query, pagination)).thenReturn(Paginated.of(BOOK_DATA));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].title", is(BOOK_DATA.getTitle())))
                .andExpect(jsonPath("$.elements[0].description", is(BOOK_DATA.getDescription())))
                .andExpect(jsonPath("$.elements[0].publicationDate", is(BOOK_DATA.getPublicationDate())))
                .andExpect(jsonPath("$.elements[0].author.id", is(BOOK_DATA.getAuthor().getId())))
                .andExpect(jsonPath("$.elements[0].author.firstName", is(BOOK_DATA.getAuthor().getFirstName())))
                .andExpect(jsonPath("$.elements[0].author.lastName", is(BOOK_DATA.getAuthor().getLastName())))
                .andExpect(jsonPath("$.elements[0].author.fullName", is(BOOK_DATA.getAuthor().getFullName())))
                .andExpect(jsonPath("$.elements[0].genre.id", is(BOOK_DATA.getGenre().getId())))
                .andExpect(jsonPath("$.elements[0].genre.name", is(BOOK_DATA.getGenre().getName())))
                .andExpect(jsonPath("$.elements[0].publisher.id", is(BOOK_DATA.getPublisher().getId())))
                .andExpect(jsonPath("$.elements[0].publisher.name", is(BOOK_DATA.getPublisher().getName())));
    }

    @Test
    void updateBook_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(put("/books/" + BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UPDATE_BOOK_REQUEST)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updateBook_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(put("/books/" + BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UPDATE_BOOK_REQUEST)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updateBook_asLibrarian_returnsOk() throws Exception {
        doNothing().when(bookService).updateBook(any());

        mockMvc.perform(put("/books/" + BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UPDATE_BOOK_REQUEST)))
                .andExpect(status().isOk());
    }

    @Test
    void removeBook_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/books/" + BOOK.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void removeBook_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/books/" + BOOK.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void removeBook_asLibrarian_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/" + BOOK.getId()))
                .andExpect(status().isNoContent());
    }
}