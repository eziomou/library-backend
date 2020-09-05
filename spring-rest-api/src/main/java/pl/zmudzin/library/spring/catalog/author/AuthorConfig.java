package pl.zmudzin.library.spring.catalog.author;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.catalog.author.AuthorReadonlyRepository;
import pl.zmudzin.library.core.application.catalog.author.AuthorService;
import pl.zmudzin.library.core.application.catalog.author.AuthorServiceImpl;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqAuthorRepository;

@Configuration
class AuthorConfig {

    @Bean
    JooqAuthorRepository jooqAuthorRepository(DSLContext context) {
        return new JooqAuthorRepository(context);
    }

    @Bean
    AuthorService authorService(AuthorRepository authorRepository, AuthorReadonlyRepository authorReadonlyRepository) {
        return new AuthorServiceImpl(authorRepository, authorReadonlyRepository);
    }
}
