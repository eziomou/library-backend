package pl.zmudzin.library.domain.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface RatingRepository {

    Rating save(Rating rating);

    Optional<Rating> findById(Long id);

    Page<Rating> findAll(Specification<Rating> specification, Pageable pageable);

    void delete(Rating rating);
}
