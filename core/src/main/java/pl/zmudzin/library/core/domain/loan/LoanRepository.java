package pl.zmudzin.library.core.domain.loan;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.Repository;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.util.Optional;

public interface LoanRepository extends Repository<Loan, LoanId> {

    Optional<Loan> find(MemberId memberId, LoanId loanId);

    Optional<Loan> findNotReturned(BookId bookId);

    boolean existsNotReturned(BookId bookId);
}
