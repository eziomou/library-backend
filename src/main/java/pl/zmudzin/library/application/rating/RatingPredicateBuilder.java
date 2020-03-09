package pl.zmudzin.library.application.rating;

import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.rating.Rating;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Custom predicate builder to avoid duplication of joins.
 *
 * @author Piotr Żmudzin
 */
public class RatingPredicateBuilder extends PredicateBuilder {

    private Root<Rating> rating;
    private Join<Rating, Member> member;
    private Join<Rating, Account> account;
    private Join<Rating, Book> book;

    private RatingPredicateBuilder(Root<Rating> rating, CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.rating = rating;
    }

    private Join<Rating, Member> getMember() {
        if (member == null) {
            member = rating.join("member", JoinType.LEFT);
        }
        return member;
    }

    private Join<Rating, Account> getAccount() {
        if (account == null) {
            account = getMember().join("account", JoinType.LEFT);
        }
        return account;
    }

    private Join<Rating, Book> getBook() {
        if (book == null) {
            book = rating.join("book", JoinType.LEFT);
        }
        return book;
    }

    public RatingPredicateBuilder memberUsername(String username) {
        equal(getAccount().get("username"), username);
        return this;
    }

    public RatingPredicateBuilder bookId(Long id) {
        equal(getBook().get("id"), id);
        return this;
    }

    public static RatingPredicateBuilder builder(Root<Rating> reservation, CriteriaBuilder criteriaBuilder) {
        return new RatingPredicateBuilder(reservation, criteriaBuilder);
    }
}
