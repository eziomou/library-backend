package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.reservation.Reservation;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class LoanTestUtil {

    public static final Long ID = 1L;
    public static final LocalDateTime LOAN_DATE = LocalDateTime.now();
    public static final LocalDateTime DUE_DATE = LOAN_DATE.plus(Reservation.MIN_DURATION);

    public static Loan mockLoan() {
        return mockLoan(ID, MemberTestUtil.mockMember(), BookTestUtil.mockBook(), LOAN_DATE, DUE_DATE);
    }

    public static Loan mockLoan(Long id, Member member, Book book, LocalDateTime loanDate, LocalDateTime dueDate) {
        Loan loan = Mockito.mock(Loan.class, withSettings().lenient());
        when(loan.getId()).thenReturn(id);
        when(loan.getMember()).thenReturn(member);
        when(loan.getBook()).thenReturn(book);
        when(loan.getLoanDate()).thenReturn(loanDate);
        when(loan.getDueDate()).thenReturn(dueDate);
        return loan;
    }
}
