package pl.zmudzin.library.application.loaning;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.loan.LoanDomainService;
import pl.zmudzin.library.domain.loan.LoanRepository;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationRepository;
import pl.zmudzin.library.util.LoanTestUtil;
import pl.zmudzin.library.util.ReservationTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanDomainService loanDomainService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void createLoan_validArguments_createsLoan() {
        Reservation reservation = ReservationTestUtil.mockReservation();
        Loan loan = LoanTestUtil.mockLoan();

        LoanCreateRequest request = new LoanCreateRequest();
        request.setReservationId(reservation.getId());

        when(reservationRepository.findById(request.getReservationId())).thenReturn(Optional.of(reservation));
        when(loanDomainService.loan(reservation)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanData data = loanService.createLoan(request);

        verify(loanRepository, times(1)).save(loan);

        assertLoanEquals(loan, data);
    }

    @Test
    void updateLoanById_completeAsLibrarian_completesLoan() {
        Loan loan = LoanTestUtil.mockLoan();
        Loan updatedLoan = LoanTestUtil.mockLoan();

        LoanUpdateRequest request = new LoanUpdateRequest();
        request.setCompleted(true);

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(authenticationService.hasAuthority(Roles.LIBRARIAN)).thenReturn(true);
        when(loanRepository.save(any())).thenReturn(updatedLoan);
        when(updatedLoan.isCompleted()).thenReturn(true);

        LoanData data = loanService.updateLoanById(loan.getId(), request);

        verify(loanDomainService, times(1)).ret(loan);

        assertLoanEquals(updatedLoan, data);
    }

    private static void assertLoanEquals(Loan loan, LoanData data) {
        assertAll(
                () -> assertEquals(loan.getMember().getAccount().getUsername(), data.getMember().getUsername()),
                () -> assertEquals(loan.getBook().getId(), data.getBook().getId()),
                () -> assertEquals(loan.getLoanDate(), data.getLoanDate()),
                () -> assertEquals(loan.getDueDate(), data.getDueDate()),
                () -> assertEquals(loan.isCompleted(), data.isCompleted())
        );
    }
}