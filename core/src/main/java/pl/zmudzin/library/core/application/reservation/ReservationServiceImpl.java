package pl.zmudzin.library.core.application.reservation;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.reservation.Reservation;
import pl.zmudzin.library.core.domain.reservation.ReservationId;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;
import pl.zmudzin.library.core.domain.reservation.ReservationRepository;

public class ReservationServiceImpl implements ReservationService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationReadonlyRepository reservationReadonlyRepository;
    private final ReservationManager reservationManager;

    public ReservationServiceImpl(MemberRepository memberRepository, BookRepository bookRepository,
                                  ReservationRepository reservationRepository, ReservationReadonlyRepository reservationReadonlyRepository, ReservationManager reservationManager) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
        this.reservationReadonlyRepository = reservationReadonlyRepository;
        this.reservationManager = reservationManager;
    }

    @Override
    public void reserveBook(String memberId, String bookId) {
        if (!memberRepository.exists(MemberId.of(memberId))) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (!bookRepository.exists(BookId.of(bookId))) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        reservationManager.reserve(MemberId.of(memberId), BookId.of(bookId));
    }

    @Override
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.find(ReservationId.of(reservationId))
                .orElseThrow(() -> newReservationNotFoundException(reservationId));
        reservationManager.cancel(reservation);
    }

    @Override
    public void rejectReservation(String reservationId) {
        Reservation reservation = reservationRepository.find(ReservationId.of(reservationId))
                .orElseThrow(() -> newReservationNotFoundException(reservationId));
        reservationManager.reject(reservation);
    }

    @Override
    public ReservationData getReservation(String reservationId) {
        return reservationReadonlyRepository.findById(ReservationId.of(reservationId))
                .orElseThrow(() -> newReservationNotFoundException(reservationId));
    }

    @Override
    public ReservationData getMemberReservation(String memberId, String reservationId) {
        return reservationReadonlyRepository.findByMemberIdAndReservationId(MemberId.of(memberId), ReservationId.of(reservationId))
                .orElseThrow(() -> newReservationNotFoundException(reservationId));
    }

    @Override
    public Paginated<ReservationData> getAllReservations(ReservationQuery query, Pagination pagination) {
        return reservationReadonlyRepository.findAllByQuery(query, pagination);
    }

    @Override
    public Paginated<ReservationData> getAllMemberReservations(String memberId, ReservationQuery query, Pagination pagination) {
        return reservationReadonlyRepository.findAllByMemberIdAndQuery(MemberId.of(memberId), query, pagination);
    }

    @Override
    public Paginated<ReservationData> getAllBookReservations(String bookId, ReservationQuery query, Pagination pagination) {
        return reservationReadonlyRepository.findAllByBookIdAndQuery(BookId.of(bookId), query, pagination);
    }

    private NotFoundException newReservationNotFoundException(String reservationId) {
        return new NotFoundException("Reservation not found: " + reservationId);
    }
}
