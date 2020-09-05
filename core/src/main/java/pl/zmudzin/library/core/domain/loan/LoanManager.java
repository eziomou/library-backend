package pl.zmudzin.library.core.domain.loan;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;

public interface LoanManager {

    void borrowBook(MemberId memberId, BookId bookId);

    void returnBook(BookId bookId);
}
