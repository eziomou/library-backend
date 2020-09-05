package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.Repository;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.util.Optional;

public interface ReservationRepository extends Repository<Reservation, ReservationId> {

    Optional<Reservation> find(MemberId memberId, ReservationId reservationId);

    boolean existsSubmitted(BookId bookId);

    boolean existsSubmitted(MemberId memberId, BookId bookId);

    Optional<Reservation> findFirstSubmitted(BookId bookId);

    boolean existsPrepared(BookId bookId);

    boolean existsPrepared(MemberId memberId, BookId bookId);

    Optional<Reservation> findFirstPrepared(BookId bookId);
}
