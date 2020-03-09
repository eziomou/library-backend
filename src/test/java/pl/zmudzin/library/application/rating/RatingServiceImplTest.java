package pl.zmudzin.library.application.rating;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.BookRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.rating.RatingDomainService;
import pl.zmudzin.library.domain.rating.RatingRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.BookTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;
import pl.zmudzin.library.util.RatingTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingDomainService ratingDomainService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void createRating_validArguments_createsRating() {
        Member member = MemberTestUtil.mockMember();
        Book book = BookTestUtil.mockBook();
        Rating rating = RatingTestUtil.mockRating(RatingTestUtil.ID, member, book, RatingTestUtil.VALUE);

        RatingCreateRequest request = new RatingCreateRequest();
        request.setBookId(book.getId());
        request.setValue(rating.getValue());

        when(authenticationService.getUsername()).thenReturn(AccountTestUtil.USERNAME);
        when(memberRepository.findByAccountUsername(member.getAccount().getUsername())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(ratingDomainService.rate(member, book, rating.getValue())).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);

        RatingData data = ratingService.createRating(request);

        verify(ratingRepository, times(1)).save(rating);
        verify(bookRepository, times(1)).save(book);

        assertRatingEquals(rating, data);
    }

    @Test
    void updateRatingById_validArguments_updatesRating() {
        Rating rating = RatingTestUtil.mockRating();
        Rating updatedRating = RatingTestUtil.mockRating();

        RatingUpdateRequest request = new RatingUpdateRequest();
        request.setValue(4);

        when(ratingRepository.findById(rating.getId())).thenReturn(Optional.of(rating));
        when(authenticationService.isResourceOwner(rating)).thenReturn(true);
        when(ratingRepository.save(rating)).thenReturn(updatedRating);
        when(updatedRating.getValue()).thenReturn(request.getValue());

        RatingData data = ratingService.updateRatingById(rating.getId(), request);

        verify(rating, times(1)).updateValue(request.getValue());
        verify(ratingRepository, times(1)).save(rating);
        verify(bookRepository, times(1)).save(updatedRating.getBook());

        assertRatingEquals(updatedRating, data);
    }

    private static void assertRatingEquals(Rating rating, RatingData data) {
        assertAll(
                () -> assertEquals(rating.getMember().getAccount().getUsername(), data.getMember().getUsername()),
                () -> assertEquals(rating.getBook().getId(), data.getBook().getId()),
                () -> assertEquals(rating.getValue(), data.getValue())
        );
    }
}