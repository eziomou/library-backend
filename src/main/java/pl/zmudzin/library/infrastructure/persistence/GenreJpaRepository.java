package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.GenreRepository;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface GenreJpaRepository extends GenreRepository, JpaRepository<Genre, Long>,
        JpaSpecificationExecutor<Genre> {
}
