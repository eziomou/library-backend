package pl.zmudzin.library.domain.loan;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class LoanTest {

    private static final Member member = Mockito.mock(Member.class);
    private static final Book book = Mockito.mock(Book.class);
    private static final LocalDateTime LOAN_DATE = LocalDateTime.now();
    private static final LocalDateTime DUE_DATE = LOAN_DATE.plus(Reservation.MIN_DURATION);

    @Test
    void constructor_nullMember_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getLoanConstructor().newInstance(null, book, LOAN_DATE, DUE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullBook_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getLoanConstructor().newInstance(member, null, LOAN_DATE, DUE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullLoanDate_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getLoanConstructor().newInstance(member, book, null, DUE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullDueDate_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getLoanConstructor().newInstance(member, book, LOAN_DATE, null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_validArguments_createdInstance() {
        assertDoesNotThrow(() -> getLoanConstructor().newInstance(member, book, LOAN_DATE, DUE_DATE));
    }

    @Test
    void complete_isCompleted_throwsException() throws Exception {
        Loan loan = createLoan();
        ReflectionUtil.setField(loan, "completed", true);

        assertThrows(IllegalStateException.class, () -> loan.complete(LocalDateTime.now()));
    }

    @Test
    void complete_isNotCompleted_completesLoan() throws Exception {
        Loan loan = createLoan();
        ReflectionUtil.setField(loan, "completed", false);

        loan.complete(LocalDateTime.now());

        assertTrue(loan.isCompleted());
    }

    private static Constructor<Loan> getLoanConstructor() throws NoSuchMethodException {
        return Loan.class.getDeclaredConstructor(Member.class, Book.class, LocalDateTime.class, LocalDateTime.class);
    }

    private static Loan createLoan() throws Exception {
        Loan loan = ReflectionUtil.getInstance(Loan.class);
        ReflectionUtil.setField(loan, "member", member);
        ReflectionUtil.setField(loan, "book", book);
        ReflectionUtil.setField(loan, "loanDate", LOAN_DATE);
        ReflectionUtil.setField(loan, "dueDate", DUE_DATE);
        return loan;
    }
}