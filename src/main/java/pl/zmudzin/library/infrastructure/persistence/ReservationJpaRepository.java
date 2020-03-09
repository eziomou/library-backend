package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface ReservationJpaRepository extends ReservationRepository, JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {

    @EntityGraph(value = "reservation-entity-graph")
    Optional<Reservation> findById(Long id);

    @EntityGraph(value = "reservation-entity-graph")
    Page<Reservation> findAll(Specification<Reservation> specification, Pageable pageable);
}