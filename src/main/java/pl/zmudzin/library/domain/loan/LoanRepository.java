package pl.zmudzin.library.domain.loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface LoanRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    Page<Loan> findAll(Specification<Loan> specification, Pageable pageable);
}
