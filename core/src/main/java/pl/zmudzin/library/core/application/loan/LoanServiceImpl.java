package pl.zmudzin.library.core.application.loan;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.loan.LoanId;
import pl.zmudzin.library.core.domain.loan.LoanManager;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;

public class LoanServiceImpl implements LoanService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanManager loanManager;
    private final LoanReadonlyRepository loanReadonlyRepository;

    public LoanServiceImpl(MemberRepository memberRepository, BookRepository bookRepository, LoanManager loanManager,
                           LoanReadonlyRepository loanReadonlyRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanManager = loanManager;
        this.loanReadonlyRepository = loanReadonlyRepository;
    }

    @Override
    public void borrowBook(String memberId, String bookId) {
        if (!memberRepository.exists(MemberId.of(memberId))) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (!bookRepository.exists(BookId.of(bookId))) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        loanManager.borrowBook(MemberId.of(memberId), BookId.of(bookId));
    }

    @Override
    public void returnBook(String bookId) {
        loanManager.returnBook(BookId.of(bookId));
    }

    @Override
    public LoanData getLoan(String loanId) {
        return loanReadonlyRepository.findById(LoanId.of(loanId))
                .orElseThrow(() -> newLoanNotFoundException(loanId));
    }

    @Override
    public LoanData getMemberLoan(String memberId, String loanId) {
        return loanReadonlyRepository.findByMemberIdAndLoanId(MemberId.of(memberId), LoanId.of(loanId))
                .orElseThrow(() -> newLoanNotFoundException(loanId));
    }

    @Override
    public Paginated<LoanData> getAllLoans(LoanQuery loanQuery, Pagination pagination) {
        return loanReadonlyRepository.findAllByQuery(loanQuery, pagination);
    }

    @Override
    public Paginated<LoanData> getAllMemberLoans(String memberId, LoanQuery loanQuery, Pagination pagination) {
        return loanReadonlyRepository.findAllByMemberIdAndQuery(MemberId.of(memberId), loanQuery, pagination);
    }

    @Override
    public Paginated<LoanData> getAllBookLoans(String bookId, LoanQuery loanQuery, Pagination pagination) {
        return loanReadonlyRepository.findAllByBookIdAndQuery(BookId.of(bookId), loanQuery, pagination);
    }

    private NotFoundException newLoanNotFoundException(String loanId) {
        return new NotFoundException("Loan not found: " + loanId);
    }
}
