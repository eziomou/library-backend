package pl.zmudzin.library.application.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface ReservationService {

    ReservationData createReservation(ReservationCreateRequest request);

    ReservationData getReservationById(Long id);

    Page<ReservationData> getAllReservations(ReservationSearchRequest request, Pageable pageable);

    ReservationData updateReservationById(Long id, ReservationUpdateRequest request);
}
