package pl.zmudzin.library.core.application.reservation;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.reservation.ReservationId;

import java.util.Optional;

public interface ReservationReadonlyRepository {

    Optional<ReservationData> findById(ReservationId reservationId);

    Optional<ReservationData> findByMemberIdAndReservationId(MemberId memberId, ReservationId reservationId);

    Paginated<ReservationData> findAllByQuery(ReservationQuery query, Pagination pagination);

    Paginated<ReservationData> findAllByMemberIdAndQuery(MemberId memberId, ReservationQuery query, Pagination pagination);

    Paginated<ReservationData> findAllByBookIdAndQuery(BookId bookId, ReservationQuery query, Pagination pagination);
}
