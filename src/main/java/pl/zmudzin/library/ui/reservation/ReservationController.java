package pl.zmudzin.library.ui.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.reservation.*;
import pl.zmudzin.library.ui.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(produces = "application/json")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(path = "/reservations", consumes = "application/json")
    public ResponseEntity<EntityModel<ReservationData>> createReservation(@Valid @RequestBody ReservationCreateRequest request) {
        ReservationData data = reservationService.createReservation(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<EntityModel<ReservationData>> getReservationById(@PathVariable Long id) {
        ReservationData data = reservationService.getReservationById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/reservations")
    public ResponseEntity<PagedModel<EntityModel<ReservationData>>> getAllReservations(@Valid ReservationSearchRequest request,
                                                                           @PageableDefault Pageable pageable) {
        Page<ReservationData> data = reservationService.getAllReservations(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/reservations/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<ReservationData>> updateReservationById(@PathVariable Long id,
                                                                  @Valid @RequestBody ReservationUpdateRequest request) {
        ReservationData data = reservationService.updateReservationById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }
}
