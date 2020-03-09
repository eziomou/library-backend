package pl.zmudzin.library.ui.catalog.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.zmudzin.library.application.catalog.author.AuthorCreateRequest;
import pl.zmudzin.library.application.catalog.author.AuthorUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.AuthorRepository;
import pl.zmudzin.library.util.AuthorTestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void createAuthor_nullFirstName_400() throws Exception {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();
        request.setFirstName(null);

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAuthor_nullLastName_400() throws Exception {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();
        request.setLastName(null);

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAuthor_notAuthenticated_401() throws Exception {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createAuthor_notAuthorized_403() throws Exception {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void createAuthor_validArguments_200() throws Exception {
        AuthorCreateRequest request = AuthorTestUtil.getAuthorCreateRequest();

        ResultActions resultActions = mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void getAuthorById_nonExistingAuthor_404() throws Exception {
        mockMvc.perform(get("/authors/" + Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAuthorById_existingAuthor_200() throws Exception {
        Author author = AuthorTestUtil.createAuthor(authorRepository);

        ResultActions resultActions = mockMvc.perform(get("/authors/" + author.getId()));

        validate(author, resultActions);
    }

    @Test
    void updateAuthor_notAuthenticated_401() throws Exception {
        AuthorCreateRequest request = new AuthorCreateRequest();

        mockMvc.perform(put("/authors/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateAuthor_notAuthorized_403() throws Exception {
        AuthorCreateRequest request = new AuthorCreateRequest();

        mockMvc.perform(put("/authors/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateAuthor_nonExistingAuthor_404() throws Exception {
        AuthorCreateRequest request = new AuthorCreateRequest();

        mockMvc.perform(put("/authors/" + Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateAuthor_validArguments_200() throws Exception {
        Author author = AuthorTestUtil.createAuthor(authorRepository);

        AuthorUpdateRequest request = new AuthorUpdateRequest();
        request.setFirstName(author.getFirstName() + "Updated");
        request.setLastName(author.getLastName() + "Updated");

        ResultActions resultActions = mockMvc.perform(put("/authors/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        validate(request, resultActions);
    }

    @Test
    void deleteAuthor_notAuthenticated_401() throws Exception {
        mockMvc.perform(delete("/authors/" + 1))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteAuthor_notAuthorized_403() throws Exception {
        mockMvc.perform(delete("/authors/" + 1))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteAuthor_notExistingAuthor_404() throws Exception {
        mockMvc.perform(delete("/authors/" + 1))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteAuthor_validArguments_200() throws Exception {
        Author author = AuthorTestUtil.createAuthor(authorRepository);

        mockMvc.perform(delete("/authors/" + author.getId()))
                .andExpect(status().isNoContent());
    }

    private static void validate(AuthorCreateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getFirstName(), request.getLastName(), resultActions);
    }

    private static void validate(AuthorUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getFirstName(), request.getLastName(), resultActions);
    }

    private static void validate(Author author, ResultActions resultActions) throws Exception {
        validate(author.getFirstName(), author.getLastName(), resultActions);
    }

    private static void validate(String firstName, String lastName, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)));
    }
}