package pl.zmudzin.library.domain.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    Page<Reservation> findAll(Specification<Reservation> specification, Pageable pageable);
}
