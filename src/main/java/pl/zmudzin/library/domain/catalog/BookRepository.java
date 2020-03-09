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
public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(Long id);

    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

    void delete(Book book);
}
