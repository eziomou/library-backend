package pl.zmudzin.library.domain.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface GenreRepository {

    Genre save(Genre genre);

    Optional<Genre> findById(Long id);

    Page<Genre> findAll(Specification<Genre> specification, Pageable pageable);

    void delete(Genre genre);
}
