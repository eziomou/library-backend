package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.BookRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface BookJpaRepository extends BookRepository, JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @EntityGraph(value = "book-entity-graph")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "book-entity-graph")
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);
}
