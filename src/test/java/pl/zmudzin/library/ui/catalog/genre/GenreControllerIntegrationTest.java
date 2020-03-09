package pl.zmudzin.library.ui.catalog.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.zmudzin.library.application.catalog.genre.GenreCreateRequest;
import pl.zmudzin.library.application.catalog.genre.GenreUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.GenreRepository;
import pl.zmudzin.library.util.GenreTestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void createGenre_nullName_400() throws Exception {
        GenreCreateRequest request = GenreTestUtil.getGenreCreateRequest();
        request.setName(null);

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGenre_notAuthenticated_401() throws Exception {
        GenreCreateRequest request = GenreTestUtil.getGenreCreateRequest();

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createGenre_notAuthorized_403() throws Exception {
        GenreCreateRequest request = GenreTestUtil.getGenreCreateRequest();

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void createGenre_validArguments_200() throws Exception {
        GenreCreateRequest request = GenreTestUtil.getGenreCreateRequest();

        ResultActions resultActions = mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void getGenreById_nonExistingGenre_404() throws Exception {
        mockMvc.perform(get("/genres/" + Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGenreById_existingGenre_200() throws Exception {
        Genre genre = GenreTestUtil.createGenre(genreRepository);

        ResultActions resultActions = mockMvc.perform(get("/genres/" + genre.getId()))
                .andExpect(status().isOk());

        validate(genre, resultActions);
    }

    @Test
    void updateGenre_notAuthenticated_401() throws Exception {
        GenreUpdateRequest request = new GenreUpdateRequest();

        mockMvc.perform(put("/genres/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateGenre_notAuthorized_403() throws Exception {
        GenreUpdateRequest request = new GenreUpdateRequest();

        mockMvc.perform(put("/genres/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateGenre_nonExistingGenre_404() throws Exception {
        GenreUpdateRequest request = new GenreUpdateRequest();

        mockMvc.perform(put("/genres/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateGenre_validArguments_200() throws Exception {
        Genre genre = GenreTestUtil.createGenre(genreRepository);

        GenreUpdateRequest request = new GenreUpdateRequest();
        request.setName(genre.getName() + "Updated");

        ResultActions resultActions = mockMvc.perform(put("/genres/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void deleteGenre_notAuthenticated_401() throws Exception {
        mockMvc.perform(delete("/genres/" + 1000))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deleteGenre_notAuthorized_403() throws Exception {
        mockMvc.perform(delete("/genres/" + 1000))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteGenre_notExistingGenre_404() throws Exception {
        mockMvc.perform(delete("/genres/" + 1000))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deleteGenre_validArguments_200() throws Exception {
        Genre genre = GenreTestUtil.createGenre(genreRepository);

        mockMvc.perform(delete("/genres/" + genre.getId()))
                .andExpect(status().isNoContent());
    }

    private static void validate(GenreCreateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getName(), resultActions);
    }

    private static void validate(GenreUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getName(), resultActions);
    }

    private static void validate(Genre genre, ResultActions resultActions) throws Exception {
        validate(genre.getName(), resultActions);
    }

    private static void validate(String name, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)));
    }
}