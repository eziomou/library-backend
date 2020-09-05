package pl.zmudzin.library.spring.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.loan.LoanData;
import pl.zmudzin.library.core.application.loan.LoanQuery;
import pl.zmudzin.library.core.application.loan.LoanService;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.loan.Loan;
import pl.zmudzin.library.core.domain.loan.LoanId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LoanControllerTest {

    private static final Loan LOAN = Loan.builder()
            .id(LoanId.of("1"))
            .memberId(MemberId.of("1"))
            .bookId(BookId.of("1"))
            .loanDate(Instant.now().plus(Duration.ofDays(5)))
            .dueDate(Instant.now().plus(Duration.ofDays(10)))
            .returned(false)
            .build();

    private static final LoanData LOAN_DATA = LoanData.builder()
            .id(LOAN.getId().toString())
            .member(builder -> builder
                    .id(LOAN.getMemberId().toString())
                    .firstName("Foo")
                    .lastName("Bar"))
            .book(builder -> builder
                    .id(LOAN.getBookId().toString())
                    .title("Foo"))
            .loanDate(LOAN.getLoanDate().toString())
            .dueDate(LOAN.getDueDate().toString())
            .returned(LOAN.isReturned())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanService loanService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void borrowBook_unauthenticated_returnsUnauthorized() throws Exception {
        BorrowBookRequest request = new BorrowBookRequest(LOAN.getMemberId().toString());

        mockMvc.perform(post("/books/" + LOAN.getBookId() + "/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void borrowBook_asMember_returnsForbidden() throws Exception {
        BorrowBookRequest request = new BorrowBookRequest(LOAN.getMemberId().toString());

        mockMvc.perform(post("/books/" + LOAN.getBookId() + "/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void borrowBook_asLibrarian_returnsOk() throws Exception {
        BorrowBookRequest request = new BorrowBookRequest(LOAN.getMemberId().toString());

        mockMvc.perform(post("/books/" + LOAN.getBookId() + "/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void returnBook_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(put("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void returnBook_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(put("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void returnBook_asLibrarian_returnsOk() throws Exception {
        mockMvc.perform(put("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLoans_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllLoans_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllLoans_asLibrarian_returnsOk() throws Exception {
        LoanQuery query = new RestLoanQuery();
        Pagination pagination = new RestPagination();

        when(loanService.getAllLoans(query, pagination))
                .thenReturn(Paginated.of(LOAN_DATA));

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(LOAN_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(LOAN_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(LOAN_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(LOAN_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(LOAN_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].loanDate", is(LOAN_DATA.getLoanDate())))
                .andExpect(jsonPath("$.elements[0].dueDate", is(LOAN_DATA.getDueDate())))
                .andExpect(jsonPath("$.elements[0].returned", is(LOAN_DATA.isReturned())));
    }

    @Test
    void getLoan_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/loans/" + LOAN.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getLoan_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/loans/" + LOAN.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getLoan_asLibrarian_returnsOk() throws Exception {
        when(loanService.getLoan(LOAN.getId().toString())).thenReturn(LOAN_DATA);

        mockMvc.perform(get("/loans/" + LOAN.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id", is(LOAN_DATA.getMember().getId())))
                .andExpect(jsonPath("$.member.firstName", is(LOAN_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.member.lastName", is(LOAN_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.book.id", is(LOAN_DATA.getBook().getId())))
                .andExpect(jsonPath("$.book.title", is(LOAN_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.loanDate", is(LOAN_DATA.getLoanDate())))
                .andExpect(jsonPath("$.dueDate", is(LOAN_DATA.getDueDate())))
                .andExpect(jsonPath("$.returned", is(LOAN_DATA.isReturned())));
    }

    @Test
    void getAllBookLoans_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllBookLoans_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllBookLoans_asLibrarian_returnsOk() throws Exception {
        LoanQuery query = new RestLoanQuery();
        Pagination pagination = new RestPagination();

        when(loanService.getAllBookLoans(LOAN.getBookId().toString(), query, pagination))
                .thenReturn(Paginated.of(LOAN_DATA));

        mockMvc.perform(get("/books/" + LOAN.getBookId() + "/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(LOAN_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(LOAN_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(LOAN_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(LOAN_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(LOAN_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].loanDate", is(LOAN_DATA.getLoanDate())))
                .andExpect(jsonPath("$.elements[0].dueDate", is(LOAN_DATA.getDueDate())))
                .andExpect(jsonPath("$.elements[0].returned", is(LOAN_DATA.isReturned())));
    }

    @Test
    void getAllMemberLoans_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/member/loans"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllMemberLoans_asMember_returnsOk() throws Exception {
        LoanQuery query = new RestLoanQuery();
        Pagination pagination = new RestPagination();

        when(loanService.getAllMemberLoans(LOAN.getMemberId().toString(), query, pagination))
                .thenReturn(Paginated.of(LOAN_DATA));

        when(authorizationService.getMemberId()).thenReturn(LOAN.getMemberId().toString());

        mockMvc.perform(get("/member/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(LOAN_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(LOAN_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(LOAN_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(LOAN_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(LOAN_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].loanDate", is(LOAN_DATA.getLoanDate())))
                .andExpect(jsonPath("$.elements[0].dueDate", is(LOAN_DATA.getDueDate())))
                .andExpect(jsonPath("$.elements[0].returned", is(LOAN_DATA.isReturned())));
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllMemberLoans_asLibrarian_returnsForbidden() throws Exception {
        mockMvc.perform(get("/member/loans"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMemberLoan_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/member/loans/" + LOAN.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getMemberLoan_asMember_returnsOk() throws Exception {
        when(loanService.getMemberLoan(LOAN.getMemberId().toString(), LOAN.getId().toString()))
                .thenReturn(LOAN_DATA);

        when(authorizationService.getMemberId()).thenReturn(LOAN.getMemberId().toString());

        mockMvc.perform(get("/member/loans/" + LOAN.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id", is(LOAN_DATA.getMember().getId())))
                .andExpect(jsonPath("$.member.firstName", is(LOAN_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.member.lastName", is(LOAN_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.book.id", is(LOAN_DATA.getBook().getId())))
                .andExpect(jsonPath("$.book.title", is(LOAN_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.loanDate", is(LOAN_DATA.getLoanDate())))
                .andExpect(jsonPath("$.dueDate", is(LOAN_DATA.getDueDate())))
                .andExpect(jsonPath("$.returned", is(LOAN_DATA.isReturned())));
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getMemberLoan_asLibrarian_returnsForbidden() throws Exception {
        mockMvc.perform(get("/member/loans/" + LOAN.getId()))
                .andExpect(status().isForbidden());
    }
}