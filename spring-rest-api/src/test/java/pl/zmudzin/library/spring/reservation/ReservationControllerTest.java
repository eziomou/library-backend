package pl.zmudzin.library.spring.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.application.reservation.ReservationData;
import pl.zmudzin.library.core.application.reservation.ReservationEventData;
import pl.zmudzin.library.core.application.reservation.ReservationQuery;
import pl.zmudzin.library.core.application.reservation.ReservationService;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.reservation.Reservation;
import pl.zmudzin.library.core.domain.reservation.ReservationId;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ReservationControllerTest {

    private static final Reservation RESERVATION = new Reservation(ReservationId.of("1"), MemberId.of("1"), BookId.of("1"), Instant.now());

    private static final ReservationData RESERVATION_DATA = ReservationData.builder()
            .id(RESERVATION.getId().toString())
            .member(builder -> builder
                    .id(RESERVATION.getMemberId().toString())
                    .firstName("Foo")
                    .lastName("Bar"))
            .book(builder -> builder
                    .id(RESERVATION.getBookId().toString())
                    .title("Foo"))
            .events(Collections.singletonList(new ReservationEventData("SUBMITTED", Instant.now().toString())))
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void reserveBook_unauthenticated_returnsUnauthorized() throws Exception {
        BookId bookId = BookId.of("1");

        mockMvc.perform(post("/books/" + bookId.toString() + "/reservations"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void reserveBook_asMember_returnsOk() throws Exception {
        MemberId memberId = MemberId.of("1");
        BookId bookId = BookId.of("1");

        doNothing().when(reservationService).reserveBook(memberId.toString(), bookId.toString());

        when(authorizationService.getMemberId()).thenReturn(memberId.toString());

        mockMvc.perform(post("/books/" + bookId.toString() + "/reservations"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void reserveBook_asLibrarian_returnsForbidden() throws Exception {
        BookId bookId = BookId.of("1");

        mockMvc.perform(post("/books/" + bookId.toString() + "/reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllReservations_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllReservations_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllReservations_asLibrarian_returnsOk() throws Exception {
        ReservationQuery query = new RestReservationQuery();
        Pagination pagination = new RestPagination();

        when(reservationService.getAllReservations(query, pagination))
                .thenReturn(Paginated.of(RESERVATION_DATA));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(RESERVATION_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(RESERVATION_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(RESERVATION_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(RESERVATION_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(RESERVATION_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].events[0].status", is(RESERVATION_DATA.getEvents().get(0).getStatus())))
                .andExpect(jsonPath("$.elements[0].events[0].occurrenceDate", is(RESERVATION_DATA.getEvents().get(0).getOccurrenceDate())));
    }

    @Test
    void getReservation_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/reservations/" + RESERVATION.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getReservation_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/reservations/" + RESERVATION.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getReservation_asLibrarian_returnsOk() throws Exception {
        when(reservationService.getReservation(RESERVATION.getId().toString())).thenReturn(RESERVATION_DATA);

        mockMvc.perform(get("/reservations/" + RESERVATION.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id", is(RESERVATION_DATA.getMember().getId())))
                .andExpect(jsonPath("$.member.firstName", is(RESERVATION_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.member.lastName", is(RESERVATION_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.book.id", is(RESERVATION_DATA.getBook().getId())))
                .andExpect(jsonPath("$.book.title", is(RESERVATION_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.events[0].status", is(RESERVATION_DATA.getEvents().get(0).getStatus())))
                .andExpect(jsonPath("$.events[0].occurrenceDate", is(RESERVATION_DATA.getEvents().get(0).getOccurrenceDate())));
    }

    @Test
    void getAllBookReservations_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/books/" + RESERVATION.getBookId() + "/reservations"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllBookReservations_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/books/" + RESERVATION.getBookId() + "/reservations"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllBookReservations_asLibrarian_returnsOk() throws Exception {
        ReservationQuery query = new RestReservationQuery();
        Pagination pagination = new RestPagination();

        when(reservationService.getAllBookReservations(RESERVATION.getBookId().toString(), query, pagination))
                .thenReturn(Paginated.of(RESERVATION_DATA));

        mockMvc.perform(get("/books/" + RESERVATION.getBookId() + "/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(RESERVATION_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(RESERVATION_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(RESERVATION_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(RESERVATION_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(RESERVATION_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].events[0].status", is(RESERVATION_DATA.getEvents().get(0).getStatus())))
                .andExpect(jsonPath("$.elements[0].events[0].occurrenceDate", is(RESERVATION_DATA.getEvents().get(0).getOccurrenceDate())));
    }

    @Test
    void getAllMemberReservations_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/member/reservations"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllMemberReservations_asMember_returnsOk() throws Exception {
        ReservationQuery query = new RestReservationQuery();
        Pagination pagination = new RestPagination();

        when(reservationService.getAllMemberReservations(RESERVATION.getMemberId().toString(), query, pagination))
                .thenReturn(Paginated.of(RESERVATION_DATA));

        when(authorizationService.getMemberId()).thenReturn(RESERVATION.getMemberId().toString());

        mockMvc.perform(get("/member/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].member.id", is(RESERVATION_DATA.getMember().getId())))
                .andExpect(jsonPath("$.elements[0].member.firstName", is(RESERVATION_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.elements[0].member.lastName", is(RESERVATION_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.elements[0].book.id", is(RESERVATION_DATA.getBook().getId())))
                .andExpect(jsonPath("$.elements[0].book.title", is(RESERVATION_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.elements[0].events[0].status", is(RESERVATION_DATA.getEvents().get(0).getStatus())))
                .andExpect(jsonPath("$.elements[0].events[0].occurrenceDate", is(RESERVATION_DATA.getEvents().get(0).getOccurrenceDate())));
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllMemberReservations_asLibrarian_returnsForbidden() throws Exception {
        mockMvc.perform(get("/member/reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMemberReservation_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/member/reservations/" + RESERVATION.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getMemberReservation_asMember_returnsOk() throws Exception {
        when(reservationService.getMemberReservation(RESERVATION.getMemberId().toString(), RESERVATION.getId().toString()))
                .thenReturn(RESERVATION_DATA);

        when(authorizationService.getMemberId()).thenReturn(RESERVATION.getMemberId().toString());

        mockMvc.perform(get("/member/reservations/" + RESERVATION.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id", is(RESERVATION_DATA.getMember().getId())))
                .andExpect(jsonPath("$.member.firstName", is(RESERVATION_DATA.getMember().getFirstName())))
                .andExpect(jsonPath("$.member.lastName", is(RESERVATION_DATA.getMember().getLastName())))
                .andExpect(jsonPath("$.book.id", is(RESERVATION_DATA.getBook().getId())))
                .andExpect(jsonPath("$.book.title", is(RESERVATION_DATA.getBook().getTitle())))
                .andExpect(jsonPath("$.events[0].status", is(RESERVATION_DATA.getEvents().get(0).getStatus())))
                .andExpect(jsonPath("$.events[0].occurrenceDate", is(RESERVATION_DATA.getEvents().get(0).getOccurrenceDate())));
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getMemberReservation_asLibrarian_returnsForbidden() throws Exception {
        mockMvc.perform(get("/member/reservations/" + RESERVATION.getId()))
                .andExpect(status().isForbidden());
    }
}
