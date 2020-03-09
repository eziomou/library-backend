package pl.zmudzin.library.application.rating;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.BookRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.rating.RatingDomainService;
import pl.zmudzin.library.domain.rating.RatingRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class RatingServiceImpl implements RatingService {

    private RatingDomainService ratingDomainService;
    private RatingRepository ratingRepository;
    private MemberRepository memberRepository;
    private BookRepository bookRepository;
    private AuthenticationService authenticationService;

    public RatingServiceImpl(RatingDomainService ratingDomainService, RatingRepository ratingRepository,
                             MemberRepository memberRepository, BookRepository bookRepository,
                             AuthenticationService authenticationService) {
        this.ratingDomainService = ratingDomainService;
        this.ratingRepository = ratingRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.authenticationService = authenticationService;
    }

    @Secured(Roles.MEMBER)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"ratings", "books"}, allEntries = true)
    @Override
    public RatingData createRating(RatingCreateRequest request) {
        Member member = memberRepository.findByAccountUsername(authenticationService.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Rating rating = ratingDomainService.rate(member, book, request.getValue());
        rating = ratingRepository.save(rating);

        bookRepository.save(book);

        return map(rating);
    }

    @Cacheable("ratings")
    @Override
    public RatingData getRatingById(Long id) {
        Rating rating = getRatingEntityById(id);
        return map(rating);
    }

    @Cacheable("ratings")
    @Override
    public Page<RatingData> getAllRatings(RatingSearchRequest request, Pageable pageable) {
        Specification<Rating> specification = (r, cq, cb) -> RatingPredicateBuilder.builder(r, cb)
                .memberUsername(request.getMemberUsername())
                .bookId(request.getBookId())
                .build();

        return ratingRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"ratings", "books"}, allEntries = true)
    @Override
    public RatingData updateRatingById(Long id, RatingUpdateRequest request) {
        Rating rating = getRatingEntityById(id);

        if (!authenticationService.isResourceOwner(rating) &&
                !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (request.getValue() != null) {
            rating.updateValue(request.getValue());
        }
        rating = ratingRepository.save(rating);
        bookRepository.save(rating.getBook());
        return map(rating);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"ratings", "books"}, allEntries = true)
    @Override
    public void deleteRatingById(Long id) {
        Rating rating = getRatingEntityById(id);

        if (!authenticationService.isResourceOwner(rating) &&
                !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        rating.getBook().removeRating(rating);

        ratingRepository.delete(rating);
        bookRepository.save(rating.getBook());
    }

    private Rating getRatingEntityById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private RatingData map(Rating rating) {
        RatingData data = new RatingData();
        data.setId(rating.getId());

        data.setMember(new MemberBasicData(
                rating.getMember().getAccount().getUsername(),
                rating.getMember().getAccount().getProfile().getFirstName(),
                rating.getMember().getAccount().getProfile().getLastName()
        ));
        data.setBook(new BookBasicData(
                rating.getBook().getId(),
                rating.getBook().getTitle()
        ));
        data.setValue(rating.getValue());
        data.setRateDate(rating.getRateDate());
        return data;
    }
}
