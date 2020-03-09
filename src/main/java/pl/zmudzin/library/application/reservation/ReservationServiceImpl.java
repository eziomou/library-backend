package pl.zmudzin.library.application.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.BookRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationDomainService;
import pl.zmudzin.library.domain.reservation.ReservationRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class ReservationServiceImpl implements ReservationService {

    private ReservationDomainService reservationDomainService;
    private AuthenticationService authenticationService;
    private MemberRepository memberRepository;
    private BookRepository bookRepository;
    private ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationDomainService reservationDomainService,
                                  AuthenticationService authenticationService, MemberRepository memberRepository,
                                  BookRepository bookRepository, ReservationRepository reservationRepository) {
        this.reservationDomainService = reservationDomainService;
        this.authenticationService = authenticationService;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
    }

    @Secured(Roles.MEMBER)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ReservationData createReservation(ReservationCreateRequest request) {
        Member member = memberRepository.findByAccountUsername(authenticationService.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Member not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Reservation reservation = reservationDomainService.reserve(member, book, request.getDuration());
        reservation = reservationRepository.save(reservation);
        return map(reservation);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Override
    public ReservationData getReservationById(Long id) {
        Reservation reservation = getReservationEntityById(id);
        return map(reservation);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Override
    public Page<ReservationData> getAllReservations(ReservationSearchRequest request, Pageable pageable) {
        Specification<Reservation> specification = (r, cq, cb) -> {
            ReservationPredicateBuilder builder = ReservationPredicateBuilder.builder(r, cb);

            if (authenticationService.hasAuthority(Roles.LIBRARIAN)) {
                builder
                        .memberUsername(request.getMemberUsername())
                        .memberFullName(request.getMemberFullName());
            } else {
                builder
                        .memberUsername(authenticationService.getUsername());
            }
            builder
                    .bookId(request.getBookId())
                    .bookTitle(request.getBookTitle())
                    .statuses(request.getStatus());

            return builder.build();
        };
        return reservationRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ReservationData updateReservationById(Long id, ReservationUpdateRequest request) {
        Reservation reservation = getReservationEntityById(id);

        boolean isLibrarian = authenticationService.hasAuthority(Roles.LIBRARIAN);

        if (request.getStatus() != null) {
            if (request.getStatus() == Reservation.Status.CANCELLED) {
                reservationDomainService.cancel(reservation);
            } else if (request.getStatus() == Reservation.Status.REJECTED && isLibrarian) {
                reservationDomainService.reject(reservation);
            }
        }
        if (request.getDuration() != null) {
            reservation.updateDuration(request.getDuration());
        }
        reservation = reservationRepository.save(reservation);
        return map(reservation);
    }

    private Reservation getReservationEntityById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!authenticationService.isResourceOwner(reservation) &&
                !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return reservation;
    }

    private ReservationData map(Reservation reservation) {
        ReservationData data = new ReservationData();
        data.setId(reservation.getId());

        data.setMember(new MemberBasicData(
                reservation.getMember().getAccount().getUsername(),
                reservation.getMember().getAccount().getProfile().getFirstName(),
                reservation.getMember().getAccount().getProfile().getLastName()
        ));
        data.setBook(new BookBasicData(
                reservation.getBook().getId(),
                reservation.getBook().getTitle()
        ));
        data.setDuration(reservation.getDuration());
        data.setStatus(reservation.getStatus());
        data.setSubmitDate(reservation.getSubmitDate());
        data.setPrepareDate(reservation.getPrepareDate());
        data.setCompleteDate(reservation.getCompleteDate());
        data.setCancelDate(reservation.getCancelDate());
        data.setRejectDate(reservation.getRejectDate());
        data.setQueuePosition(reservation.getQueuePosition());
        return data;
    }
}
