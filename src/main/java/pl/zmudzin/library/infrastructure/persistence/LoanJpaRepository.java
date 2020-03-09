package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.loan.LoanRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface LoanJpaRepository extends LoanRepository, JpaRepository<Loan, Long>,
        JpaSpecificationExecutor<Loan> {

    @EntityGraph(value = "loan-entity-graph")
    Optional<Loan> findById(Long id);

    @EntityGraph(value = "loan-entity-graph")
    Page<Loan> findAll(Specification<Loan> specification, Pageable pageable);
}
