package pl.zmudzin.library.spring.catalog.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.catalog.author.AuthorData;
import pl.zmudzin.library.core.application.catalog.author.AuthorQuery;
import pl.zmudzin.library.core.application.catalog.author.AuthorService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthorControllerTest {

    private static final Author AUTHOR = new Author(AuthorId.of("1"), "Foo", "Bar");

    private static final AuthorData AUTHOR_DATA = new AuthorData(AUTHOR.getId().toString(), AUTHOR.getFirstName(), AUTHOR.getLastName());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    void addAuthor_unauthenticated_returnsUnauthorized() throws Exception {
        AddAuthorRequest request = new AddAuthorRequest("Foo", "Bar");

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void addAuthor_asMember_returnsForbidden() throws Exception {
        AddAuthorRequest request = new AddAuthorRequest("Foo", "Bar");

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void addAuthor_asLibrarian_returnsOk() throws Exception {
        AddAuthorRequest request = new AddAuthorRequest("Foo", "Bar");

        doNothing().when(authorService).addAuthor(request.getFirstName(), request.getLastName());

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getAuthorById_existingAuthor_returnsOk() throws Exception {
        when(authorService.getAuthorById(AUTHOR.getId().toString())).thenReturn(AUTHOR_DATA);

        mockMvc.perform(get("/authors/" + AUTHOR.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(AUTHOR_DATA.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(AUTHOR_DATA.getLastName())))
                .andExpect(jsonPath("$.fullName", is(AUTHOR_DATA.getFullName())));
    }

    @Test
    void getAllAuthorsByQuery_validRequest_returnsOk() throws Exception {
        AuthorQuery query = new RestAuthorQuery();
        Pagination pagination = new RestPagination();

        when(authorService.getAllAuthorsByQuery(query, pagination)).thenReturn(Paginated.of(AUTHOR_DATA));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].firstName", is(AUTHOR_DATA.getFirstName())))
                .andExpect(jsonPath("$.elements[0].lastName", is(AUTHOR_DATA.getLastName())))
                .andExpect(jsonPath("$.elements[0].fullName", is(AUTHOR_DATA.getFullName())));
    }

    @Test
    void updateAuthor_unauthenticated_returnsUnauthorized() throws Exception {
        UpdateAuthorRequest request = new UpdateAuthorRequest("Baz", "Qux");

        mockMvc.perform(put("/authors/" + AUTHOR.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updateAuthor_asMember_returnsForbidden() throws Exception {
        UpdateAuthorRequest request = new UpdateAuthorRequest("Baz", "Qux");

        mockMvc.perform(put("/authors/" + AUTHOR.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updateAuthor_asLibrarian_returnsOk() throws Exception {
        UpdateAuthorRequest request = new UpdateAuthorRequest("Baz", "Qux");

        doNothing().when(authorService).updateAuthor(AUTHOR.getId().toString(), request.getFirstName(), request.getLastName());

        mockMvc.perform(put("/authors/" + AUTHOR.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void removeAuthor_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/authors/" + AUTHOR.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void removeAuthor_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/authors/" + AUTHOR.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void removeAuthor_asLibrarian_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/authors/" + AUTHOR.getId()))
                .andExpect(status().isNoContent());
    }
}