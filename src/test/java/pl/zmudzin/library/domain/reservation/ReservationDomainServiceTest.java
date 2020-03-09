package pl.zmudzin.library.domain.reservation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
class ReservationDomainServiceTest {

    private static final Member MEMBER = Mockito.mock(Member.class);
    private static final Book BOOK = Mockito.mock(Book.class);
    private static final Duration DURATION = Reservation.MIN_DURATION;

    private ReservationDomainService reservationDomainService = new ReservationDomainService(Clock.systemDefaultZone());

    @Test
    void reserve_memberHasSubmittedReservationOfRequestedBook_throwsException() throws Exception {
        Reservation reservation = createReservation(1L, Reservation.Status.SUBMITTED);
        when(MEMBER.getReservations()).thenReturn(Collections.singletonList(reservation));

        assertThrows(IllegalArgumentException.class, () -> reservationDomainService.reserve(MEMBER, BOOK, DURATION));
    }

    @Test
    void reserve_memberHasPreparedReservationOfRequestedBook_throwsException() throws Exception {
        Reservation reservation = createReservation(1L, Reservation.Status.PREPARED);
        when(MEMBER.getReservations()).thenReturn(Collections.singletonList(reservation));

        assertThrows(IllegalArgumentException.class, () -> reservationDomainService.reserve(MEMBER, BOOK, DURATION));
    }

    @Test
    void reserve_bookHasNoReservations_returnsPreparedReservation() {
        when(BOOK.getReservations()).thenReturn(Collections.emptyList());

        Reservation reservation = reservationDomainService.reserve(MEMBER, BOOK, DURATION);

        assertEquals(reservation.getStatus(), Reservation.Status.PREPARED);
    }

    @Test
    void reserve_bookHasPreparedReservation_returnsSubmittedReservation() throws Exception {
        Reservation otherReservation = createReservation(1L, Reservation.Status.PREPARED);
        when(BOOK.getReservations()).thenReturn(Collections.singletonList(otherReservation));

        Reservation reservation = reservationDomainService.reserve(MEMBER, BOOK, DURATION);

        assertEquals(reservation.getStatus(), Reservation.Status.SUBMITTED);
    }

    @Test
    void reserve_bookHasCompletedReservation_returnsSubmittedReservation() throws Exception {
        Reservation otherReservation = createReservation(1L, Reservation.Status.COMPLETED);
        when(MEMBER.getReservations()).thenReturn(Collections.singletonList(otherReservation));

        Reservation reservation = reservationDomainService.reserve(MEMBER, BOOK, DURATION);

        assertEquals(reservation.getStatus(), Reservation.Status.PREPARED);
    }

    @Test
    void cancel_bookHasOtherSubmittedReservation_preparesOtherReservation() throws Exception {
        Reservation reservation = createReservation(1L, Reservation.Status.PREPARED);
        Reservation otherReservation = createReservation(2L, Reservation.Status.SUBMITTED);

        when(BOOK.getReservations()).thenReturn(Arrays.asList(reservation, otherReservation));
        when(BOOK.getReservations(Reservation.Status.SUBMITTED)).thenReturn(Collections.singletonList(otherReservation));

        reservationDomainService.cancel(reservation);

        assertAll(
                () -> assertEquals(reservation.getStatus(), Reservation.Status.CANCELLED),
                () -> assertEquals(otherReservation.getStatus(), Reservation.Status.PREPARED)
        );
    }

    @Test
    void reject_bookHasOtherSubmittedReservation_preparesOtherReservation() throws Exception {
        Reservation reservation = createReservation(1L, Reservation.Status.PREPARED);
        Reservation otherReservation = createReservation(2L, Reservation.Status.SUBMITTED);

        when(BOOK.getReservations()).thenReturn(Arrays.asList(reservation, otherReservation));
        when(BOOK.getReservations(Reservation.Status.SUBMITTED)).thenReturn(Collections.singletonList(otherReservation));

        reservationDomainService.reject(reservation);

        assertAll(
                () -> assertEquals(reservation.getStatus(), Reservation.Status.REJECTED),
                () -> assertEquals(otherReservation.getStatus(), Reservation.Status.PREPARED)
        );
    }

    private static Reservation createReservation(Long id, Reservation.Status status)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Reservation reservation = ReflectionUtil.getInstance(Reservation.class);
        ReflectionUtil.setField(reservation, "id", id);
        ReflectionUtil.setField(reservation, "member", MEMBER);
        ReflectionUtil.setField(reservation, "book", BOOK);
        ReflectionUtil.setField(reservation, "status", status);
        return reservation;
    }
}