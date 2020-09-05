package pl.zmudzin.library.core.application.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.reservation.Reservation;
import pl.zmudzin.library.core.domain.reservation.ReservationId;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;
import pl.zmudzin.library.core.domain.reservation.ReservationRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationReadonlyRepository reservationReadonlyRepository;

    @Mock
    private ReservationManager reservationManager;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void reserveBook_notExistingMember_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> reservationService.reserveBook(memberId.toString(), bookId.toString()));
    }

    @Test
    void reserveBook_notExistingBook_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> reservationService.reserveBook(memberId.toString(), bookId.toString()));
    }

    @Test
    void reserveBook_existingMemberAndBook_reservesBook() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(true);

        reservationService.reserveBook(memberId.toString(), bookId.toString());

        verify(reservationManager, times(1)).reserve(memberId, bookId);
    }

    @Test
    void cancelReservation_notExistingReservation_throwsException() {
        ReservationId reservationId = ReservationId.of("");

        when(reservationRepository.find(reservationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.cancelReservation(reservationId.toString()));

        verify(reservationManager, times(0)).cancel(any());
    }

    @Test
    void cancelReservation_existingReservation_cancelsReservation() {
        ReservationId reservationId = ReservationId.of("");
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());

        when(reservationRepository.find(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(reservationId.toString());

        verify(reservationManager, times(1)).cancel(reservation);
    }

    @Test
    void rejectReservation_notExistingReservation_throwsException() {
        ReservationId reservationId = ReservationId.of("");

        when(reservationRepository.find(reservationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.rejectReservation(reservationId.toString()));

        verify(reservationManager, times(0)).cancel(any());
    }

    @Test
    void rejectReservation_existingReservation_rejectsReservation() {
        ReservationId reservationId = ReservationId.of("");
        Reservation reservation = new Reservation(ReservationId.of(""), MemberId.of(""), BookId.of(""), Instant.now());

        when(reservationRepository.find(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.rejectReservation(reservationId.toString());

        verify(reservationManager, times(1)).reject(reservation);
    }
}