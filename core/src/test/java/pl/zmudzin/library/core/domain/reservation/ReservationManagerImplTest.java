package pl.zmudzin.library.core.domain.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationManagerImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ReservationManagerImpl reservationManager;

    @Test
    void reserve_memberAlreadyHasSubmittedReservation_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(reservationRepository.existsSubmitted(memberId, bookId)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> reservationManager.reserve(memberId, bookId));
        assertEquals("Member already has submitted reservation for this book", exception.getMessage());
    }

    @Test
    void reserve_memberAlreadyHasPreparedReservation_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(reservationRepository.existsSubmitted(memberId, bookId)).thenReturn(false);
        when(reservationRepository.existsPrepared(memberId, bookId)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> reservationManager.reserve(memberId, bookId));
        assertEquals("Member already has prepared reservation for this book", exception.getMessage());
    }

    @Test
    void reserve_memberHasNoReservation_submitsReservation() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(reservationRepository.existsSubmitted(memberId, bookId)).thenReturn(false);
        when(reservationRepository.existsPrepared(memberId, bookId)).thenReturn(false);

        reservationManager.reserve(memberId, bookId);

        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void tryPrepare_thereIsAlreadyPreparedReservation_doesNothing() {
        BookId bookId = BookId.of("");

        when(reservationRepository.existsPrepared(bookId)).thenReturn(true);

        reservationManager.tryPrepare(bookId);

        verify(reservationRepository, times(0)).update(any());
    }

    @Test
    void tryPrepare_thereIsNoPreparedReservationButBookIsNotReturned_doesNothing() {
        BookId bookId = BookId.of("");

        when(reservationRepository.existsPrepared(bookId)).thenReturn(false);
        when(loanRepository.existsNotReturned(bookId)).thenReturn(true);

        reservationManager.tryPrepare(bookId);

        verify(reservationRepository, times(0)).update(any());
    }

    @Test
    void tryPrepare_thereIsNoPreparedReservationAndBookIsReturned_preparesReservation() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");
        Reservation reservation = new Reservation(ReservationId.of(""), memberId, bookId, Instant.now());

        when(reservationRepository.existsPrepared(bookId)).thenReturn(false);
        when(loanRepository.existsNotReturned(bookId)).thenReturn(false);

        when(reservationRepository.findFirstSubmitted(bookId)).thenReturn(Optional.of(reservation));

        reservationManager.tryPrepare(bookId);

        verify(reservationRepository, times(1)).update(any());
    }

    @Test
    void tryRealize_thereIsNoPreparedReservation_doesNothing() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(reservationRepository.findFirstPrepared(bookId)).thenReturn(Optional.empty());

        reservationManager.tryRealize(memberId, bookId);

        verify(reservationRepository, times(0)).update(any());
    }

    @Test
    void tryRealize_thereIsPreparedReservationButForAnotherMember_throwsException() {
        MemberId memberId = MemberId.of("1");
        BookId bookId = BookId.of("");
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of("2"), bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));

        when(reservationRepository.findFirstPrepared(bookId)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> reservationManager.tryRealize(memberId, bookId));

        verify(reservationRepository, times(0)).update(any());
    }

    @Test
    void tryRealize_thereIsPreparedReservation_realizesReservation() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");
        Reservation reservation = new Reservation(ReservationId.of(""), memberId, bookId, Instant.now());
        reservation = reservation.prepare(Instant.now().plusSeconds(1));

        when(reservationRepository.findFirstPrepared(bookId)).thenReturn(Optional.of(reservation));

        reservationManager.tryRealize(memberId, bookId);

        verify(reservationRepository, times(1)).update(any());
    }

    @Test
    void cancel_validStatus_cancelsReservation() {
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());

        reservationManager.cancel(reservation);

        verify(reservationRepository, times(1)).update(any());
    }

    @Test
    void cancel_invalidStatus_throwsException() {
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());
        Reservation cancelled = reservation.cancel(Instant.now().plusSeconds(1));

        assertThrows(IllegalStateException.class, () -> reservationManager.cancel(cancelled));

        verifyNoInteractions(reservationRepository);
    }

    @Test
    void reject_validStatus_rejectsReservation() {
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());

        reservationManager.reject(reservation);

        verify(reservationRepository, times(1)).update(any());
    }

    @Test
    void reject_invalidStatus_throwsException() {
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());
        Reservation cancelled = reservation.reject(Instant.now().plusSeconds(1));

        assertThrows(IllegalStateException.class, () -> reservationManager.reject(cancelled));

        verifyNoInteractions(reservationRepository);
    }
}