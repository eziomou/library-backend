package pl.zmudzin.library.core.application.rating;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.rating.Rating;
import pl.zmudzin.library.core.domain.rating.RatingId;
import pl.zmudzin.library.core.domain.rating.RatingRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class RatingServiceImpl implements RatingService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(MemberRepository memberRepository, BookRepository bookRepository,
                             RatingRepository ratingRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void rateBook(String memberId, String bookId, int value) {
        if (!memberRepository.exists(MemberId.of(memberId))) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (!bookRepository.exists(BookId.of(bookId))) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        ratingRepository.find(MemberId.of(memberId), BookId.of(bookId)).ifPresent(ratingRepository::delete);
        Rating rating = createRating(memberId, bookId, value);
        ratingRepository.save(rating);
    }

    private Rating createRating(String memberId, String bookId, int value) {
        RatingId ratingId = new RatingId(UUID.randomUUID().toString());
        return new Rating(ratingId, MemberId.of(memberId), BookId.of(bookId), value, Instant.now());
    }

    @Override
    public Optional<Double> calculateAverageRating(String bookId) {
        return ratingRepository.calculateAverageRating(BookId.of(bookId));
    }
}
