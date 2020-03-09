package pl.zmudzin.library.domain.reservation;

import pl.zmudzin.ddd.annotations.domain.DomainServiceImpl;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.util.DateTimeUtil;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Piotr Żmudzin
 */
@DomainServiceImpl
public class ReservationDomainService {

    private Clock clock;

    public ReservationDomainService(Clock clock) {
        this.clock = clock;
    }

    public Reservation reserve(Member member, Book book, Duration duration) {
        if (hasReservation(member, book)) {
            throw new IllegalArgumentException("Member has " + Reservation.Status.SUBMITTED + " or " +
                    Reservation.Status.PREPARED + " reservation of requested book");
        }
        LocalDateTime submitDate = DateTimeUtil.skipWeekends(LocalDateTime.now(clock));
        Reservation reservation = new Reservation(member, book, duration, submitDate);

        if (canPrepareNextReservation(book)) {
            LocalDateTime prepareDate = DateTimeUtil.skipWeekends(LocalDateTime.now(clock));
            reservation.prepare(prepareDate);
        }
        return reservation;
    }

    private boolean hasReservation(Member member, Book book) {
        for (Reservation r : member.getReservations()) {
            if (r.getBook().equals(book) &&
                    (r.getStatus() == Reservation.Status.SUBMITTED || r.getStatus() == Reservation.Status.PREPARED)) {
                return true;
            }
        }
        return false;
    }

    private boolean canPrepareNextReservation(Book book) {
        return !book.isLoaned() &&
                book.getReservations().stream().noneMatch(r -> r.getStatus() == Reservation.Status.PREPARED);
    }

    private void prepareNextReservation(Book book) {
        if (!canPrepareNextReservation(book)) {
            return;
        }
        List<Reservation> reservations = book.getReservations(Reservation.Status.SUBMITTED);

        if (reservations.size() > 0) {
            LocalDateTime prepareDate = DateTimeUtil.skipWeekends(LocalDateTime.now(clock));
            reservations.get(0).prepare(prepareDate);
        }
    }

    public void cancel(Reservation reservation) {
        Reservation.Status previousStatus = reservation.getStatus();

        LocalDateTime cancelDate = LocalDateTime.now(clock);
        reservation.cancel(cancelDate);

        if (previousStatus == Reservation.Status.PREPARED) {
            prepareNextReservation(reservation.getBook());
        }
    }

    public void reject(Reservation reservation) {
        Reservation.Status previousStatus = reservation.getStatus();

        LocalDateTime rejectDate = LocalDateTime.now(clock);
        reservation.reject(rejectDate);

        if (previousStatus == Reservation.Status.PREPARED) {
            prepareNextReservation(reservation.getBook());
        }
    }
}
