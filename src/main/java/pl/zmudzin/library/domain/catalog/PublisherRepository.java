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
public interface PublisherRepository {

    Publisher save(Publisher publisher);

    Optional<Publisher> findById(Long id);

    Page<Publisher> findAll(Specification<Publisher> specification, Pageable pageable);

    void delete(Publisher publisher);
}
