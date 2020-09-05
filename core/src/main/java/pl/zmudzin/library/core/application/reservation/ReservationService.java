package pl.zmudzin.library.core.application.reservation;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface ReservationService {

    void reserveBook(String memberId, String bookId);

    void cancelReservation(String reservationId);

    void rejectReservation(String reservationId);

    ReservationData getReservation(String reservationId);

    ReservationData getMemberReservation(String memberId, String reservationId);

    Paginated<ReservationData> getAllReservations(ReservationQuery query, Pagination pagination);

    Paginated<ReservationData> getAllMemberReservations(String memberId, ReservationQuery query, Pagination pagination);

    Paginated<ReservationData> getAllBookReservations(String bookId, ReservationQuery query, Pagination pagination);
}
