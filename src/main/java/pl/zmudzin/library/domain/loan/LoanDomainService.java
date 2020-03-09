package pl.zmudzin.library.domain.loan;

import pl.zmudzin.ddd.annotations.domain.DomainServiceImpl;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.util.DateTimeUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Piotr Żmudzin
 */
@DomainServiceImpl
public class LoanDomainService {

    private Clock clock;

    public LoanDomainService(Clock clock) {
        this.clock = clock;
    }

    public Loan loan(Reservation reservation) {
        if (reservation.getBook().isLoaned()) {
            throw new IllegalStateException("The requested book is already loaned");
        }
        reservation.getBook().setLoaned(true);

        LocalDateTime completeDate = LocalDateTime.now(clock);
        reservation.complete(completeDate);

        LocalDateTime loanDate = DateTimeUtil.skipWeekends(LocalDateTime.now(clock));
        LocalDateTime dueDate = DateTimeUtil.skipWeekends(loanDate.plus(reservation.getDuration()));

        return new Loan(reservation.getMember(), reservation.getBook(), loanDate, dueDate);
    }

    public void ret(Loan loan) {
        if (!loan.getBook().isLoaned()) {
            throw new IllegalStateException("The specified book is already returned");
        }
        loan.getBook().setLoaned(false);

        LocalDateTime completeDate = LocalDateTime.now(clock);
        loan.complete(completeDate);

        prepareNextReservation(loan);
    }

    private void prepareNextReservation(Loan loan) {
        List<Reservation> reservations = loan.getBook().getReservations(Reservation.Status.SUBMITTED);

        if (reservations.size() > 0) {
            LocalDateTime prepareDate = LocalDateTime.now(clock);
            reservations.get(0).prepare(prepareDate);
        }
    }
}
