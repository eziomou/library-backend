package pl.zmudzin.library.spring.catalog.publisher;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherReadonlyRepository;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherService;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherServiceImpl;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqPublisherRepository;

@Configuration
class PublisherConfig {

    @Bean
    JooqPublisherRepository jooqPublisherRepository(DSLContext context) {
        return new JooqPublisherRepository(context);
    }

    @Bean
    PublisherService publisherService(PublisherRepository publisherRepository,
                                      PublisherReadonlyRepository publisherReadonlyRepository) {
        return new PublisherServiceImpl(publisherRepository, publisherReadonlyRepository);
    }
}
