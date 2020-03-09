package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.AuthorRepository;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface AuthorJpaRepository extends AuthorRepository, JpaRepository<Author, Long>,
        JpaSpecificationExecutor<Author> {
}
