package pl.zmudzin.library.ui.loaning;

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
import pl.zmudzin.library.application.loaning.LoanCreateRequest;
import pl.zmudzin.library.application.loaning.LoanUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.catalog.*;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.loan.LoanDomainService;
import pl.zmudzin.library.domain.loan.LoanRepository;
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
class LoaningControllerIntegrationTest {

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

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void createLoan_notAuthenticated_401() throws Exception {
        LoanCreateRequest request = new LoanCreateRequest();
        request.setReservationId(1L);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createLoan_notAuthorized_403() throws Exception {
        LoanCreateRequest request = new LoanCreateRequest();
        request.setReservationId(1L);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void createLoan_asLibrarian_200() throws Exception {
        Reservation reservation = createReservation();

        LoanCreateRequest request = new LoanCreateRequest();
        request.setReservationId(reservation.getId());

        ResultActions resultActions = mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getLoanById_notAuthenticated_401() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getLoanById_notAuthorized_403() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void getLoanById_asOwner_200() throws Exception {
        Loan loan = createLoan();

        ResultActions resultActions = mockMvc.perform(get("/loans/" + loan.getId()));

        validate(loan, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void getLoanById_asLibrarian_200() throws Exception {
        Loan loan = createLoan();

        ResultActions resultActions = mockMvc.perform(get("/loans/" + loan.getId()));

        validate(loan, resultActions);
    }

    @Test
    void updateLoanById_notAuthenticated_401() throws Exception {
        LoanUpdateRequest loanUpdateRequest = new LoanUpdateRequest();

        mockMvc.perform(put("/loans/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loanUpdateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateLoanById_notAuthorized_401() throws Exception {
        LoanUpdateRequest loanUpdateRequest = new LoanUpdateRequest();

        mockMvc.perform(put("/loans/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loanUpdateRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateLoanById_asLibrarian_200() throws Exception {
        Loan loan = createLoan();

        LoanUpdateRequest loanUpdateRequest = new LoanUpdateRequest();

        ResultActions resultActions = mockMvc.perform(put("/loans/" + loan.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loanUpdateRequest)));

        validate(loan, resultActions);
    }

    private Reservation createReservation() {
        Account account = AccountTestUtil.getAccount(accountRepository);
        Member member = MemberTestUtil.getMember(memberRepository, account);

        Book book = BookTestUtil.createBook(bookRepository, authorRepository, genreRepository, publisherRepository);

        ReservationDomainService reservationDomainService = new ReservationDomainService(Clock.systemDefaultZone());
        Reservation reservation = reservationDomainService.reserve(member, book, Duration.ofDays(7));

        return reservationRepository.save(reservation);
    }

    private Loan createLoan() {
        Reservation reservation = createReservation();

        LoanDomainService loanDomainService = new LoanDomainService(Clock.systemDefaultZone());
        Loan loan = loanDomainService.loan(reservation);

        return loanRepository.save(loan);
    }

    private static void validate(Loan loan, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(loan.getId().intValue())))
                .andExpect(jsonPath("$.member.username", is(AccountTestUtil.USERNAME)))
                .andExpect(jsonPath("$.book.id", is(loan.getBook().getId().intValue())));
    }
}