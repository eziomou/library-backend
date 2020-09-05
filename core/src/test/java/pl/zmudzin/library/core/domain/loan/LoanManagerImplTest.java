package pl.zmudzin.library.core.domain.loan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;
import pl.zmudzin.library.core.domain.reservation.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class LoanManagerImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationManager reservationManager;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanManagerImpl loanManager;

    @Test
    void borrowBook_pass() {

    }

    @Test
    void returnBook_pass() {

    }
}
