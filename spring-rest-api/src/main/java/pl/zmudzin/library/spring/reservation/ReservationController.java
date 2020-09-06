package pl.zmudzin.library.spring.reservation;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.reservation.ReservationData;
import pl.zmudzin.library.core.application.reservation.ReservationService;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.catalog.book.BookController;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthorizationService authorizationService;

    public ReservationController(ReservationService reservationService, AuthorizationService authorizationService) {
        this.reservationService = reservationService;
        this.authorizationService = authorizationService;
    }

    @Transactional
    @Secured({Role.MEMBER})
    @PostMapping(path = BookController.BASE_PATH + "/{bookId}/reservations")
    public ResponseEntity<?> reserveBook(@PathVariable String bookId) {
        reservationService.reserveBook(authorizationService.getMemberId(), bookId);
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllReservations(@Valid RestReservationQuery query, @Valid RestPagination pagination) {
        Paginated<ReservationData> reservations = reservationService.getAllReservations(query, pagination);
        return ResponseEntity.ok(reservations);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(value = "/reservations/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReservation(@PathVariable String reservationId) {
        ReservationData reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(value = "/members/{memberId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMemberReservations(@PathVariable String memberId, @Valid RestReservationQuery query,
                                                      @Valid RestPagination pagination) {
        Paginated<ReservationData> reservations = reservationService.getAllMemberReservations(memberId, query, pagination);
        return ResponseEntity.ok(reservations);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(value = "/books/{bookId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBookReservations(@PathVariable String bookId, @Valid RestReservationQuery query,
                                                    @Valid RestPagination pagination) {
        Paginated<ReservationData> reservations = reservationService.getAllBookReservations(bookId, query, pagination);
        return ResponseEntity.ok(reservations);
    }

    @Secured({Role.MEMBER})
    @GetMapping(value = "/member/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMemberReservations(@Valid RestReservationQuery query, @Valid RestPagination pagination) {
        Paginated<ReservationData> reservations = reservationService.getAllMemberReservations(authorizationService.getMemberId(), query, pagination);
        return ResponseEntity.ok(reservations);
    }

    @Secured({Role.MEMBER})
    @GetMapping(value = "/member/reservations/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMemberReservation(@PathVariable String reservationId) {
        ReservationData reservation = reservationService.getMemberReservation(authorizationService.getMemberId(), reservationId);
        return ResponseEntity.ok(reservation);
    }
}
