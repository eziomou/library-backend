package pl.zmudzin.library.core.domain.loan;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class LoanManagerImpl implements LoanManager {

    private static final Duration LOAN_DURATION = Duration.ofDays(20);

    private final ReservationManager reservationManager;
    private final LoanRepository loanRepository;

    public LoanManagerImpl(ReservationManager reservationManager, LoanRepository loanRepository) {
        this.reservationManager = reservationManager;
        this.loanRepository = loanRepository;
    }

    @Override
    public void borrowBook(MemberId memberId, BookId bookId) {
        checkBook(bookId);
        reservationManager.tryRealize(memberId, bookId);
        Loan loan = createLoan(memberId, bookId);
        loanRepository.save(loan);
    }

    private void checkBook(BookId bookId) {
        if (!isReturned(bookId)) {
            throw new IllegalStateException("Book is not returned: " + bookId);
        }
    }

    private boolean isReturned(BookId bookId) {
        return loanRepository.findNotReturned(bookId).isEmpty();
    }

    private Loan createLoan(MemberId memberId, BookId bookId) {
        Instant loanDate = Instant.now();
        Instant dueDate = loanDate.plus(LOAN_DURATION);

        return Loan.builder()
                .id(new LoanId(UUID.randomUUID().toString()))
                .memberId(memberId)
                .bookId(bookId)
                .loanDate(loanDate)
                .dueDate(dueDate)
                .returned(false)
                .build();
    }

    @Override
    public void returnBook(BookId bookId) {
        loanRepository.findNotReturned(bookId).ifPresentOrElse(loan -> {
            loan = loan.returnLoan();
            loanRepository.update(loan);
            reservationManager.tryPrepare(bookId);
        }, () -> {
            throw new IllegalStateException("Book is not borrowed: " + bookId);
        });
    }
}
