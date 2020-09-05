package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;
import java.util.UUID;

public class ReservationManagerImpl implements ReservationManager {

    private final ReservationRepository reservationRepository;
    private final LoanRepository loanRepository;

    public ReservationManagerImpl(ReservationRepository reservationRepository, LoanRepository loanRepository) {
        this.reservationRepository = reservationRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public void reserve(MemberId memberId, BookId bookId) {
        if (reservationRepository.existsSubmitted(memberId, bookId)) {
            throw new IllegalStateException("Member already has submitted reservation for this book");
        }
        if (reservationRepository.existsPrepared(memberId, bookId)) {
            throw new IllegalStateException("Member already has prepared reservation for this book");
        }
        Reservation reservation = createReservation(memberId, bookId);
        reservationRepository.save(reservation);
        tryPrepare(bookId);
    }

    private Reservation createReservation(MemberId memberId, BookId bookId) {
        ReservationId reservationId = new ReservationId(UUID.randomUUID().toString());
        return new Reservation(reservationId, memberId, bookId, Instant.now());
    }

    @Override
    public void tryPrepare(BookId bookId) {
        if (!isPrepared(bookId) && isReturned(bookId)) {
            reservationRepository.findFirstSubmitted(bookId).ifPresent(reservation -> {
                reservation = reservation.prepare(Instant.now());
                reservationRepository.update(reservation);
            });
        }
    }

    private boolean isPrepared(BookId bookId) {
        return reservationRepository.existsPrepared(bookId);
    }

    private boolean isReturned(BookId bookId) {
        return !loanRepository.existsNotReturned(bookId);
    }

    @Override
    public void tryRealize(MemberId memberId, BookId bookId) {
        reservationRepository.findFirstPrepared(bookId).ifPresent(reservation ->  {
            if (isSubmitter(reservation, memberId)) {
                reservation = reservation.realize(Instant.now());
                reservationRepository.update(reservation);
            } else {
                throw new IllegalStateException("Book is reserved for another member");
            }
        });
    }

    private boolean isSubmitter(Reservation reservation, MemberId memberId) {
        return reservation.getMemberId().equals(memberId);
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation = reservation.cancel(Instant.now());
        reservationRepository.update(reservation);
        tryPrepare(reservation.getBookId());
    }

    @Override
    public void reject(Reservation reservation) {
        reservation = reservation.cancel(Instant.now());
        reservationRepository.update(reservation);
        tryPrepare(reservation.getBookId());
    }
}
