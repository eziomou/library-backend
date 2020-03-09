package pl.zmudzin.library.domain.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class LoanDomainServiceTest {

    private static final Member MEMBER = Mockito.mock(Member.class);

    private Book book;
    private Reservation reservation;
    private Loan loan;

    private LoanDomainService loanDomainService = new LoanDomainService(Clock.systemDefaultZone());

    @BeforeEach
    void beforeEach() {
        try {
            book = ReflectionUtil.getInstance(Book.class);

            reservation = ReflectionUtil.getInstance(Reservation.class);
            ReflectionUtil.setField(reservation, "member", MEMBER);
            ReflectionUtil.setField(reservation, "book", book);
            ReflectionUtil.setField(reservation, "duration", Duration.ofDays(7));

            loan = ReflectionUtil.getInstance(Loan.class);
            ReflectionUtil.setField(loan, "member", MEMBER);
            ReflectionUtil.setField(loan, "book", book);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loan_bookIsLoaned_throwsException() {
        ReflectionUtil.setField(book, "loaned", true);
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);
        assertThrows(IllegalStateException.class, () -> loanDomainService.loan(reservation));
    }

    @Test
    void loan_preparedReservation_loansBook() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        Loan loan = loanDomainService.loan(reservation);

        assertAll(
                () -> assertTrue(book.isLoaned()),
                () -> assertFalse(loan.isCompleted())
        );
    }

    @Test
    void ret_bookIsNotLoaned_throwsException() {
        ReflectionUtil.setField(book, "loaned", false);
        ReflectionUtil.setField(loan, "completed", false);

        assertThrows(IllegalStateException.class, () -> loanDomainService.ret(loan));
    }

    @Test
    void ret_loanIsCompleted_throwsException() {
        ReflectionUtil.setField(book, "loaned", true);
        ReflectionUtil.setField(loan, "completed", true);

        assertThrows(IllegalStateException.class, () -> loanDomainService.ret(loan));
    }

    @Test
    void ret_bookIsLoanedAndLoanIsNotCompleted_returnsLoan() {
        ReflectionUtil.setField(book, "loaned", true);
        ReflectionUtil.setField(loan, "completed", false);

        loanDomainService.ret(loan);

        assertAll(
                () -> assertFalse(book.isLoaned()),
                () -> assertTrue(loan.isCompleted())
        );
    }
}