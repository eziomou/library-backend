package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.reservation.ReservationCreateRequest;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.reservation.Reservation;

import java.time.Duration;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class ReservationTestUtil {

    public static final Long ID = 1L;
    public static final Duration DURATION = Reservation.MIN_DURATION;

    public static ReservationCreateRequest getReservationCreateRequest() {
        return getReservationCreateRequest(BookTestUtil.ID, DURATION);
    }

    public static ReservationCreateRequest getReservationCreateRequest(Long bookId, Duration duration) {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setBookId(bookId);
        request.setDuration(duration);
        return request;
    }

    public static Reservation mockReservation() {
        return mockReservation(ID, MemberTestUtil.mockMember(), BookTestUtil.mockBook(), DURATION);
    }

    public static Reservation mockReservation(Long id, Member member, Book book, Duration duration) {
        Reservation reservation = Mockito.mock(Reservation.class, withSettings().lenient());
        when(reservation.getId()).thenReturn(id);
        when(reservation.getMember()).thenReturn(member);
        when(reservation.getBook()).thenReturn(book);
        when(reservation.getDuration()).thenReturn(duration);
        return reservation;
    }
}
