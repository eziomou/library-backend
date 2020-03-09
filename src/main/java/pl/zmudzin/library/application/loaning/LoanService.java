package pl.zmudzin.library.application.loaning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface LoanService {

    LoanData createLoan(LoanCreateRequest request);

    LoanData getLoanById(Long id);

    Page<LoanData> getAllLoans(LoanSearchRequest request, Pageable pageable);

    LoanData updateLoanById(Long id, LoanUpdateRequest request);
}
