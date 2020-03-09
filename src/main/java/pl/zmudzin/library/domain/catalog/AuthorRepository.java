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
public interface AuthorRepository {

    Author save(Author author);

    Optional<Author> findById(Long id);

    Page<Author> findAll(Specification<Author> specification, Pageable pageable);

    void delete(Author author);
}
