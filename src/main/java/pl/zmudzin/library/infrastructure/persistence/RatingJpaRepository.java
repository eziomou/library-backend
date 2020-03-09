package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.rating.RatingRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface RatingJpaRepository extends RatingRepository, JpaRepository<Rating, Long>,
        JpaSpecificationExecutor<Rating> {

    @EntityGraph(value = "rating-entity-graph")
    Optional<Rating> findById(Long id);

    @EntityGraph(value = "rating-entity-graph")
    Page<Rating> findAll(Specification<Rating> specification, Pageable pageable);
}
