package pl.zmudzin.library.core.domain.reservation;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private final ReservationId reservationId = ReservationId.of("");
    private final MemberId memberId = MemberId.of("");
    private final BookId bookId = BookId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Reservation(null, null, null, (Instant) null));
    }

    @Test
    void constructor_nullMember_throwsException() {
        assertThrows(NullPointerException.class, () -> new Reservation(reservationId, null, null, (Instant) null));
    }

    @Test
    void constructor_nullBook_throwsException() {
        assertThrows(NullPointerException.class, () -> new Reservation(reservationId, memberId, null, (Instant) null));
    }

    @Test
    void constructor_nullSubmitDate_throwsException() {
        assertThrows(NullPointerException.class, () -> new Reservation(reservationId, memberId, bookId, (Instant) null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Reservation(reservationId, memberId, bookId, Instant.now()));
    }

    @Test
    void submit_newlyCreatedReservation_submitsReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        assertEquals(ReservationStatus.SUBMITTED, reservation.getStatus());
    }

    @Test
    void prepare_submittedReservation_preparesReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));
        assertEquals(ReservationStatus.PREPARED, reservation.getStatus());
    }

    @Test
    void realize_preparedReservation_completesReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));
        reservation = reservation.realize(Instant.now().plusSeconds(2));
        assertEquals(ReservationStatus.REALIZED, reservation.getStatus());
    }

    @Test
    void cancel_submittedReservation_cancelsReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.cancel(Instant.now().plusSeconds(1));
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    void cancel_preparedReservation_cancelsReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));
        reservation = reservation.cancel(Instant.now().plusSeconds(2));
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    void reject_submittedReservation_rejectsReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.reject(Instant.now().plusSeconds(1));
        assertEquals(ReservationStatus.REJECTED, reservation.getStatus());
    }

    @Test
    void reject_preparedReservation_rejectsReservation() {
        Reservation reservation = new Reservation(reservationId, memberId, bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));
        reservation = reservation.reject(Instant.now().plusSeconds(2));
        assertEquals(ReservationStatus.REJECTED, reservation.getStatus());
    }
}