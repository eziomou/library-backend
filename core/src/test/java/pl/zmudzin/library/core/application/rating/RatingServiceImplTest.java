package pl.zmudzin.library.core.application.rating;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.rating.RatingRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void rateBook_notExistingMember_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ratingService.rateBook(memberId.toString(), bookId.toString(), 5));
    }

    @Test
    void rateBook_notExistingBook_throwsException() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ratingService.rateBook(memberId.toString(), bookId.toString(), 5));
    }

    @Test
    void rateBook_existingMemberAndBook_reservesBook() {
        MemberId memberId = MemberId.of("");
        BookId bookId = BookId.of("");

        when(memberRepository.exists(memberId)).thenReturn(true);
        when(bookRepository.exists(bookId)).thenReturn(true);

        ratingService.rateBook(memberId.toString(), bookId.toString(), 5);

        verify(ratingRepository, times(1)).save(any());
    }
}