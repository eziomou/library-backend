package pl.zmudzin.library.spring.rating;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.rating.RatingService;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.rating.Rating;
import pl.zmudzin.library.core.domain.rating.RatingId;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RatingControllerTest {

    private static final Rating RATING = new Rating(RatingId.of("1"), MemberId.of("1"), BookId.of("1"), 5, Instant.now());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void rateBook_unauthenticated_returnsUnauthorized() throws Exception {
        RateBookRequest request = new RateBookRequest(5);

        mockMvc.perform(post("/books/" + RATING.getBookId() + "/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void rateBook_asMember_returnsOk() throws Exception {
        RateBookRequest request = new RateBookRequest(5);

        mockMvc.perform(post("/books/" + RATING.getBookId() + "/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void rateBook_asLibrarian_returnsForbidden() throws Exception {
        RateBookRequest request = new RateBookRequest(5);

        mockMvc.perform(post("/books/" + RATING.getBookId() + "/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void calculateAverageRating_hasRating_returnsOk() throws Exception {
        when(ratingService.calculateAverageRating(RATING.getBookId().toString())).thenReturn(Optional.of(5.0));

        mockMvc.perform(get("/books/" + RATING.getBookId() + "/ratings/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(5.0)));
    }

    @Test
    void calculateAverageRating_hasNoRating_returnsNotFound() throws Exception {
        when(ratingService.calculateAverageRating(RATING.getBookId().toString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/" + RATING.getBookId() + "/ratings/average"))
                .andExpect(status().isNotFound());
    }
}