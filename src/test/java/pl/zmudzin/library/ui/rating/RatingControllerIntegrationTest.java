package pl.zmudzin.library.ui.rating;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import pl.zmudzin.library.application.rating.RatingCreateRequest;
import pl.zmudzin.library.application.rating.RatingUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.catalog.*;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.rating.RatingDomainService;
import pl.zmudzin.library.domain.rating.RatingRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.BookTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;

import java.time.Clock;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class RatingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void createRating_notAuthenticated_401() throws Exception {
        RatingCreateRequest request = getRatingCreateRequest(1L);

        mockMvc.perform(post("/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createRating_notAuthorized_403() throws Exception {
        RatingCreateRequest request = getRatingCreateRequest(1L);

        mockMvc.perform(post("/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void createRating_asMember_200() throws Exception {
        createMember();
        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        RatingCreateRequest request = getRatingCreateRequest(book.getId());

        ResultActions resultActions = mockMvc.perform(post("/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        validate(request, resultActions);
    }

    @Test
    void getRating_existingRating_200() throws Exception {
        Rating rating = createRating();

        ResultActions resultActions = mockMvc.perform(get("/ratings/" + rating.getId()));

        validate(rating, resultActions);
    }

    @Test
    void updateRatingById_notAuthenticated_401() throws Exception {
        RatingUpdateRequest request = new RatingUpdateRequest();

        mockMvc.perform(put("/ratings/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateRatingById_notAuthorized_403() throws Exception {
        RatingUpdateRequest request = new RatingUpdateRequest();

        mockMvc.perform(put("/ratings/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void updateRatingById_asMember_200() throws Exception {
        Rating rating = createRating();

        RatingUpdateRequest request = new RatingUpdateRequest();
        request.setValue(4);

        ResultActions resultActions = mockMvc.perform(put("/ratings/" + rating.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        validate(request, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateRatingById_asLibrarian_200() throws Exception {
        Rating rating = createRating();

        RatingUpdateRequest request = new RatingUpdateRequest();
        request.setValue(4);

        ResultActions resultActions = mockMvc.perform(put("/ratings/" + rating.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        validate(request, resultActions);
    }

    private static RatingCreateRequest getRatingCreateRequest(Long bookId) {
        RatingCreateRequest request = new RatingCreateRequest();
        request.setBookId(bookId);
        request.setValue(5);
        return request;
    }

    private Member createMember() {
        Account account = AccountTestUtil.getAccount(accountRepository);
        return MemberTestUtil.getMember(memberRepository, account);
    }

    private Rating createRating() {
        Member member = createMember();

        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        RatingDomainService ratingDomainService = new RatingDomainService(Clock.systemDefaultZone());
        Rating rating = ratingDomainService.rate(member, book, 5);

        return ratingRepository.save(rating);
    }

    private static void validate(RatingCreateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getValue(), resultActions);
    }

    private static void validate(RatingUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getValue(), resultActions);
    }

    private static void validate(Rating rating, ResultActions resultActions) throws Exception {
        validate(rating.getValue(), resultActions);
    }

    private static void validate(int value, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(value)));
    }
}