package pl.zmudzin.library.spring.catalog.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.catalog.genre.GenreData;
import pl.zmudzin.library.core.application.catalog.genre.GenreQuery;
import pl.zmudzin.library.core.application.catalog.genre.GenreService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class GenreControllerTest {

    private static final Genre GENRE = new Genre(GenreId.of("1"), "Foo");

    private static final GenreData GENRE_DATA = new GenreData(GENRE.getId().toString(), GENRE.getName());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @Test
    void addGenre_unauthenticated_returnsUnauthorized() throws Exception {
        AddGenreRequest request = new AddGenreRequest("Foo");

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void addGenre_asMember_returnsForbidden() throws Exception {
        AddGenreRequest request = new AddGenreRequest("Foo");

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void addGenre_asLibrarian_returnsOk() throws Exception {
        AddGenreRequest request = new AddGenreRequest("Foo");

        doNothing().when(genreService).addGenre(request.getName());

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getGenreById_existingGenre_returnsOk() throws Exception {
        when(genreService.getGenreById(GENRE.getId().toString())).thenReturn(GENRE_DATA);

        mockMvc.perform(get("/genres/" + GENRE.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(GENRE_DATA.getName())));
    }

    @Test
    void getAllGenresByQuery_validRequest_returnsOk() throws Exception {
        GenreQuery query = new RestGenreQuery();
        Pagination pagination = new RestPagination();

        when(genreService.getAllGenresByQuery(query, pagination)).thenReturn(Paginated.of(GENRE_DATA));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].name", is(GENRE_DATA.getName())));
    }

    @Test
    void updateGenre_unauthenticated_returnsUnauthorized() throws Exception {
        UpdateGenreRequest request = new UpdateGenreRequest("Bar");

        mockMvc.perform(put("/genres/" + GENRE.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updateGenre_asMember_returnsForbidden() throws Exception {
        UpdateGenreRequest request = new UpdateGenreRequest("Bar");

        mockMvc.perform(put("/genres/" + GENRE.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updateGenre_asLibrarian_returnsOk() throws Exception {
        UpdateGenreRequest request = new UpdateGenreRequest("Bar");

        doNothing().when(genreService).updateGenre(GENRE.getId().toString(), request.getName());

        mockMvc.perform(put("/genres/" + GENRE.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void removeGenre_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/genres/" + GENRE.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void removeGenre_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/genres/" + GENRE.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void removeGenre_asLibrarian_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/genres/" + GENRE.getId()))
                .andExpect(status().isNoContent());
    }
}