package pl.zmudzin.library.ui.reservation;

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
import pl.zmudzin.library.application.reservation.ReservationCreateRequest;
import pl.zmudzin.library.application.reservation.ReservationUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.catalog.*;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationDomainService;
import pl.zmudzin.library.domain.reservation.ReservationRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.BookTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;

import java.time.Clock;
import java.time.Duration;

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
class ReservationControllerIntegrationTest {

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
    private ReservationRepository reservationRepository;

    @Test
    void createReservation_nullBookId_400() throws Exception {
        ReservationCreateRequest request = getReservationCreateRequest(null);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservation_nullDuration_400() throws Exception {
        ReservationCreateRequest request = getReservationCreateRequest(1L);
        request.setDuration(null);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservation_notAuthenticated_401() throws Exception {
        ReservationCreateRequest request = getReservationCreateRequest(1L);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void createReservation_notExistingBook_404() throws Exception {
        createMember();

        ReservationCreateRequest request = getReservationCreateRequest(Long.MAX_VALUE);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void createReservation_validArguments_200() throws Exception {
        createMember();

        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);
        ReservationCreateRequest request = getReservationCreateRequest(book.getId());

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.username", is(AccountTestUtil.USERNAME)))
                .andExpect(jsonPath("$.book.id", is(request.getBookId().intValue())))
                .andExpect(jsonPath("$.duration", is(request.getDuration().toString())));
    }

    private static ReservationCreateRequest getReservationCreateRequest(Long bookId) {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setBookId(bookId);
        request.setDuration(Duration.ofDays(7));
        return request;
    }

    @Test
    void getReservationById_notAuthenticated_401() throws Exception {
        mockMvc.perform(get("/reservations/" + 1))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getReservationById_notAuthorized_403() throws Exception {
        mockMvc.perform(get("/reservations/" + 1))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void getReservationById_asOwner_200() throws Exception {
        Member member = createMember();
        Reservation reservation = createReservation(member);

        validate(reservation, mockMvc.perform(get("/reservations/" + reservation.getId())));
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void getReservationById_asLibrarian_200() throws Exception {
        Member member = createMember();
        Reservation reservation = createReservation(member);

        validate(reservation, mockMvc.perform(get("/reservations/" + reservation.getId())));
    }

    @Test
    void updateReservationById_notAuthenticated_401() throws Exception {
        mockMvc.perform(put("/reservations/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ReservationUpdateRequest())))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateReservationById_notAuthorized_403() throws Exception {
        mockMvc.perform(put("/reservations/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ReservationUpdateRequest())))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void updateReservationById_cancelAsOwner_200() throws Exception {
        Member member = createMember();
        Reservation reservation = createReservation(member);

        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setStatus(Reservation.Status.CANCELLED);

        ResultActions resultActions = mockMvc.perform(put("/reservations/" + reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        reservation = reservationRepository.findById(reservation.getId()).get();

        validate(reservation, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateReservationById_rejectAsLibrarian_200() throws Exception {
        Member member = createMember();
        Reservation reservation = createReservation(member);

        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setStatus(Reservation.Status.REJECTED);

        ResultActions resultActions = mockMvc.perform(put("/reservations/" + reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ReservationUpdateRequest())));

        validate(reservation, resultActions);
    }

    private void validate(Reservation reservation, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reservation.getId().intValue())))
                .andExpect(jsonPath("$.member.username", is(AccountTestUtil.USERNAME)))
                .andExpect(jsonPath("$.book.id", is(reservation.getBook().getId().intValue())))
                .andExpect(jsonPath("$.book.title", is(reservation.getBook().getTitle())))
                .andExpect(jsonPath("$.duration", is(reservation.getDuration().toString())))
                .andExpect(jsonPath("$.status", is(reservation.getStatus().toString())));
    }

    private Member createMember() {
        Account account = AccountTestUtil.getAccount(accountRepository);
        return MemberTestUtil.getMember(memberRepository, account);
    }

    private Reservation createReservation(Member member) {
        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        ReservationDomainService reservationDomainService = new ReservationDomainService(Clock.systemDefaultZone());
        Reservation reservation = reservationDomainService.reserve(member, book, Duration.ofDays(7));

        return reservationRepository.save(reservation);
    }
}