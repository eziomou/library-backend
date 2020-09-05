package pl.zmudzin.library.spring.catalog.book;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.catalog.book.BookReadonlyRepository;
import pl.zmudzin.library.core.application.catalog.book.BookService;
import pl.zmudzin.library.core.application.catalog.book.BookServiceImpl;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqBookRepository;

@Configuration
class BookConfig {

    @Bean
    JooqBookRepository jooqBookRepository(DSLContext context) {
        return new JooqBookRepository(context);
    }

    @Bean
    BookService bookService(BookRepository bookRepository, BookReadonlyRepository bookReadonlyRepository,
                            AuthorRepository authorRepository, GenreRepository genreRepository,
                            PublisherRepository publisherRepository) {
        return new BookServiceImpl(bookRepository, bookReadonlyRepository, authorRepository, genreRepository,
                publisherRepository);
    }
}
