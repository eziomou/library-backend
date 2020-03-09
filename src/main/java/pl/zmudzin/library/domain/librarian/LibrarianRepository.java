package pl.zmudzin.library.domain.librarian;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface LibrarianRepository {

    Librarian save(Librarian member);

    Optional<Librarian> findById(Long id);

    boolean existsByAccountId(Long id);

    Page<Librarian> findAll(Specification<Librarian> specification, Pageable pageable);
}
