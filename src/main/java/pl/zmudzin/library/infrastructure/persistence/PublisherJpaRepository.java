package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.catalog.Publisher;
import pl.zmudzin.library.domain.catalog.PublisherRepository;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface PublisherJpaRepository extends PublisherRepository, JpaRepository<Publisher, Long>,
        JpaSpecificationExecutor<Publisher> {
}
