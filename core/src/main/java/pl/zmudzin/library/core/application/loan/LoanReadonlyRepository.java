package pl.zmudzin.library.core.application.loan;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.loan.LoanId;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.util.Optional;

public interface LoanReadonlyRepository {

    Optional<LoanData> findById(LoanId loanId);

    Optional<LoanData> findByMemberIdAndLoanId(MemberId memberId, LoanId loanId);

    Paginated<LoanData> findAllByQuery(LoanQuery query, Pagination pagination);

    Paginated<LoanData> findAllByMemberIdAndQuery(MemberId memberId, LoanQuery query, Pagination pagination);

    Paginated<LoanData> findAllByBookIdAndQuery(BookId bookId, LoanQuery query, Pagination pagination);
}
