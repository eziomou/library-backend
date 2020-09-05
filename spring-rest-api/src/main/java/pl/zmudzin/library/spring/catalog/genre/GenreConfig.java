package pl.zmudzin.library.spring.catalog.genre;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.catalog.genre.GenreReadonlyRepository;
import pl.zmudzin.library.core.application.catalog.genre.GenreService;
import pl.zmudzin.library.core.application.catalog.genre.GenreServiceImpl;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqGenreRepository;

@Configuration
class GenreConfig {

    @Bean
    JooqGenreRepository jooqGenreRepository(DSLContext context) {
        return new JooqGenreRepository(context);
    }

    @Bean
    GenreService genreService(GenreRepository genreRepository, GenreReadonlyRepository genreReadonlyRepository) {
        return new GenreServiceImpl(genreRepository, genreReadonlyRepository);
    }
}
