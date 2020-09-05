package pl.zmudzin.library.spring.librarian;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.domain.librarian.LibrarianRepository;
import pl.zmudzin.library.persistence.jooq.librarian.JooqLibrarianRepository;

@Configuration
class LibrarianConfig {

    @Bean
    LibrarianRepository librarianRepository(DSLContext context) {
        return new JooqLibrarianRepository(context);
    }
}
