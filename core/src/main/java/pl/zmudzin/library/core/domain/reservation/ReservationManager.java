package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.member.MemberId;

public interface ReservationManager {

    void reserve(MemberId member, BookId book);

    void tryPrepare(BookId bookId);

    void tryRealize(MemberId memberId, BookId bookId);

    void cancel(Reservation reservation);

    void reject(Reservation reservation);
}
