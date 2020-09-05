package pl.zmudzin.library.spring.rating;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.rating.RatingService;
import pl.zmudzin.library.core.application.rating.RatingServiceImpl;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.rating.RatingRepository;
import pl.zmudzin.library.persistence.jooq.rating.JooqRatingRepository;

@Configuration
class RatingConfig {

    @Bean
    RatingRepository ratingRepository(DSLContext context) {
        return new JooqRatingRepository(context);
    }

    @Bean
    RatingService ratingService(MemberRepository memberRepository, BookRepository bookRepository,
                                RatingRepository ratingRepository) {
        return new RatingServiceImpl(memberRepository, bookRepository, ratingRepository);
    }
}
