package pl.zmudzin.library.core.domain.loan;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.reservation.ReservationId;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {

    private final ReservationId reservationId = ReservationId.of("");
    private final MemberId memberId = MemberId.of("");
    private final BookId bookId = BookId.of("");

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> preparedBuilder().id(null).build());
    }

    @Test
    void constructor_nullMemberId_throwsException() {
        assertThrows(NullPointerException.class, () -> preparedBuilder().memberId(null).build());
    }

    @Test
    void constructor_nullBookId_throwsException() {
        assertThrows(NullPointerException.class, () -> preparedBuilder().bookId(null).build());
    }

    @Test
    void constructor_nullLoanDate_throwsException() {
        assertThrows(NullPointerException.class, () -> preparedBuilder().loanDate(null).build());
    }

    @Test
    void constructor_nullDueDate_throwsException() {
        assertThrows(NullPointerException.class, () -> preparedBuilder().dueDate(null).build());
    }

    @Test
    void returnLoan_notReturnedLoan_returnsLoan() {
        Loan loan = preparedBuilder().build();
        assertFalse(loan.isReturned());
        loan = loan.returnLoan();
        assertTrue(loan.isReturned());
    }

    @Test
    void returnLoan_alreadyReturnedLoan_throwsException() {
        Loan loan = preparedBuilder().build();
        Loan returned = loan.returnLoan();
        assertThrows(IllegalStateException.class, returned::returnLoan);
    }

    private Loan.Builder preparedBuilder() {
        return Loan.builder()
                .id(LoanId.of(""))
                .memberId(MemberId.of(""))
                .bookId(BookId.of(""))
                .loanDate(Instant.now().plus(Duration.ofDays(5)))
                .dueDate(Instant.now().plus(Duration.ofDays(10)))
                .returned(false);
    }
}
