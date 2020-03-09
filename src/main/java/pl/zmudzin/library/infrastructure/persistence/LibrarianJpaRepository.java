package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.librarian.Librarian;
import pl.zmudzin.library.domain.librarian.LibrarianRepository;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface LibrarianJpaRepository extends LibrarianRepository, JpaRepository<Librarian, Long>,
        JpaSpecificationExecutor<Librarian> {
}
