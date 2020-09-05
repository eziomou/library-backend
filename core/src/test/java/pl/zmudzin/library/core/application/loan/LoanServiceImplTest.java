package pl.zmudzin.library.core.application.loan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.loan.LoanManager;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanManager loanManager;

    @Mock
    private LoanReadonlyRepository loanReadonlyRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void borrowBook_notExistingMember_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> loanService.borrowBook(memberId.toString(), bookId.toString()));
    }

    @Test
    void borrowBook_notExistingBook_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> loanService.borrowBook(memberId.toString(), bookId.toString()));
    }

    @Test
    void borrowBook_existingMemberAndBook_reservesBook() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(true);

        loanService.borrowBook(memberId.toString(), bookId.toString());

        verify(loanManager, times(1)).borrowBook(memberId, bookId);
    }

    @Test
    void returnBook_anyState_returnsBook() {
        BookId bookId = BookId.of("");

        loanService.returnBook(bookId.toString());

        verify(loanManager, times(1)).returnBook(bookId);
    }
}