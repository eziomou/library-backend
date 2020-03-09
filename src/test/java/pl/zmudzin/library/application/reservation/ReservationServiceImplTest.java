package pl.zmudzin.library.application.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.BookRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationDomainService;
import pl.zmudzin.library.domain.reservation.ReservationRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.BookTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;
import pl.zmudzin.library.util.ReservationTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationDomainService reservationDomainService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation_validArguments_createsReservation() {
        Member member = MemberTestUtil.mockMember();
        Book book = BookTestUtil.mockBook();
        Reservation reservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);

        ReservationCreateRequest request = ReservationTestUtil.getReservationCreateRequest();

        when(authenticationService.getUsername()).thenReturn(AccountTestUtil.USERNAME);
        when(memberRepository.findByAccountUsername(member.getAccount().getUsername())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(reservationDomainService.reserve(member, book, Reservation.MIN_DURATION)).thenReturn(reservation);
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationData data = reservationService.createReservation(request);

        assertReservationEquals(reservation, data);
    }

    @Test
    void updateReservationById_updateDurationAsOwner_updatesReservation() {
        Member member = MemberTestUtil.mockMember();
        Book book = BookTestUtil.mockBook();
        Reservation reservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);
        Reservation updatedReservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MAX_DURATION);

        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setDuration(updatedReservation.getDuration());

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(authenticationService.isResourceOwner(reservation)).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(updatedReservation);

        ReservationData data = reservationService.updateReservationById(reservation.getId(), request);

        verify(reservation, times(1)).updateDuration(request.getDuration());
        verify(reservationRepository, times(1)).save(any());

        assertReservationEquals(updatedReservation, data);
    }

    @Test
    void updateReservationById_cancelAsOwner_cancelsReservation() {
        Member member = MemberTestUtil.mockMember();
        Book book = BookTestUtil.mockBook();
        Reservation reservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);

        Reservation updatedReservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);
        when(updatedReservation.getStatus()).thenReturn(Reservation.Status.CANCELLED);

        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setStatus(Reservation.Status.CANCELLED);

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(authenticationService.isResourceOwner(reservation)).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(updatedReservation);

        ReservationData data = reservationService.updateReservationById(reservation.getId(), request);

        verify(reservationDomainService, times(1)).cancel(reservation);
        verify(reservationRepository, times(1)).save(any());

        assertReservationEquals(updatedReservation, data);
        assertEquals(updatedReservation.getStatus(), data.getStatus());
    }

    @Test
    void updateReservationById_rejectAsLibrarian_rejectsReservation() {
        Member member = MemberTestUtil.mockMember();
        Book book = BookTestUtil.mockBook();
        Reservation reservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);

        Reservation updatedReservation = ReservationTestUtil.mockReservation(1L, member, book, Reservation.MIN_DURATION);
        when(updatedReservation.getStatus()).thenReturn(Reservation.Status.REJECTED);

        ReservationUpdateRequest request = new ReservationUpdateRequest();
        request.setStatus(Reservation.Status.REJECTED);

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(authenticationService.isResourceOwner(reservation)).thenReturn(false);
        when(authenticationService.hasAuthority(Roles.LIBRARIAN)).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(updatedReservation);

        ReservationData data = reservationService.updateReservationById(reservation.getId(), request);

        verify(reservationDomainService, times(1)).reject(reservation);
        verify(reservationRepository, times(1)).save(any());

        assertReservationEquals(updatedReservation, data);
        assertEquals(updatedReservation.getStatus(), data.getStatus());
    }

    private static void assertReservationEquals(Reservation reservation, ReservationData data) {
        assertAll(
                () -> assertEquals(reservation.getBook().getId(), data.getBook().getId()),
                () -> assertEquals(reservation.getDuration(), data.getDuration())
        );
    }
}