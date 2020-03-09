package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.rating.Rating;

import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
public class RatingTestUtil {

    public static final Long ID = 1L;
    public static final int VALUE = 5;

    public static Rating mockRating() {
        return mockRating(ID, MemberTestUtil.mockMember(), BookTestUtil.mockBook(), VALUE);
    }

    public static Rating mockRating(Long id, Member member, Book book, int value) {
        Rating rating = Mockito.mock(Rating.class, withSettings().lenient());
        when(rating.getId()).thenReturn(id);
        when(rating.getMember()).thenReturn(member);
        when(rating.getBook()).thenReturn(book);
        when(rating.getValue()).thenReturn(value);
        return rating;
    }
}
