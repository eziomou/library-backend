package pl.zmudzin.library.core.application.loan;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface LoanService {

    void borrowBook(String memberId, String bookId);

    void returnBook(String bookId);

    LoanData getLoan(String loanId);

    LoanData getMemberLoan(String memberId, String loanId);

    Paginated<LoanData> getAllLoans(LoanQuery loanQuery, Pagination pagination);

    Paginated<LoanData> getAllMemberLoans(String memberId, LoanQuery loanQuery, Pagination pagination);

    Paginated<LoanData> getAllBookLoans(String bookId, LoanQuery loanQuery, Pagination pagination);
}
