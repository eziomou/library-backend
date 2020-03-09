package pl.zmudzin.library.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.util.ReflectionUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Piotr Żmudzin
 */
class MemberTest {

    private final static Account ACCOUNT = Mockito.mock(Account.class);

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = new Member(ACCOUNT);
    }

    @Test
    void constructor_nullAccount_throwsException() {
        assertThrows(NullPointerException.class, () -> new Member(null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Member(ACCOUNT));
    }

    @Test
    void addReservation_nullReservation_throwsException() {
        assertThrows(NullPointerException.class, () -> member.addReservation(null));
    }

    @Test
    void addReservation_hasDifferentMember_throwsException() {
        ReflectionUtil.setField(member, "id", 1L);

        Member otherMember = new Member(ACCOUNT);
        ReflectionUtil.setField(otherMember, "id", member.getId() + 1L);

        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservation.getMember()).thenReturn(otherMember);

        assertThrows(IllegalStateException.class, () -> member.addReservation(reservation));
    }

    @Test
    void addReservation_validReservation_addsReservation() {
        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservation.getMember()).thenReturn(member);

        member.addReservation(reservation);

        assertTrue(member.getReservations().contains(reservation));
    }

    @Test
    void addLoan_nullLoan_throwsException() {
        assertThrows(NullPointerException.class, () -> member.addLoan(null));
    }

    @Test
    void addLoan_hasDifferentMember_throwsException() {
        ReflectionUtil.setField(member, "id", 1L);

        Member otherMember = new Member(ACCOUNT);
        ReflectionUtil.setField(otherMember, "id", member.getId() + 1L);

        Loan loan = Mockito.mock(Loan.class);
        when(loan.getMember()).thenReturn(otherMember);

        assertThrows(IllegalStateException.class, () -> member.addLoan(loan));
    }

    @Test
    void addLoan_validLoan_addsLoan() {
        Loan loan = Mockito.mock(Loan.class);
        when(loan.getMember()).thenReturn(member);

        member.addLoan(loan);

        assertTrue(member.getLoans().contains(loan));
    }

    @Test
    void addRating_nullRating_throwsException() {
        assertThrows(NullPointerException.class, () -> member.addRating(null));
    }

    @Test
    void addRating_hasDifferentMember_throwsException() {
        ReflectionUtil.setField(member, "id", 1L);

        Member otherMember = new Member(ACCOUNT);
        ReflectionUtil.setField(otherMember, "id", member.getId() + 1L);

        Rating rating = Mockito.mock(Rating.class);
        when(rating.getMember()).thenReturn(otherMember);

        assertThrows(IllegalStateException.class, () -> member.addRating(rating));
    }

    @Test
    void addRating_validRating_addsRating() {
        Rating rating = Mockito.mock(Rating.class);
        when(rating.getMember()).thenReturn(member);

        member.addRating(rating);

        assertTrue(member.getRatings().contains(rating));
    }
}