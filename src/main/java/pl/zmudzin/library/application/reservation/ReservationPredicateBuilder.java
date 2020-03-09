package pl.zmudzin.library.application.reservation;

import pl.zmudzin.library.application.util.PersonPredicateBuilder;
import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.reservation.Reservation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * Custom predicate builder to avoid duplication of joins.
 *
 * @author Piotr Żmudzin
 */
public class ReservationPredicateBuilder extends PredicateBuilder {

    private Root<Reservation> reservation;
    private Join<Reservation, Member> member;
    private Join<Reservation, Account> account;
    private Join<Reservation, Book> book;

    private ReservationPredicateBuilder(Root<Reservation> reservation, CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.reservation = reservation;
    }

    private Join<Reservation, Member> getMember() {
        if (member == null) {
            member = reservation.join("member", JoinType.LEFT);
        }
        return member;
    }

    private Join<Reservation, Account> getAccount() {
        if (account == null) {
            account = getMember().join("account", JoinType.LEFT);
        }
        return account;
    }

    private Join<Reservation, Book> getBook() {
        if (book == null) {
            book = reservation.join("book", JoinType.LEFT);
        }
        return book;
    }

    public ReservationPredicateBuilder memberUsername(String username) {
        equal(getAccount().get("username"), username);
        return this;
    }

    public ReservationPredicateBuilder memberFullName(String fullName) {
        like(PersonPredicateBuilder.getFullName(getAccount().get("profile"), criteriaBuilder), fullName);
        return this;
    }

    public ReservationPredicateBuilder bookId(Long id) {
        equal(getBook().get("id"), id);
        return this;
    }

    public ReservationPredicateBuilder bookTitle(String title) {
        like(getBook().get("title"), title);
        return this;
    }

    public ReservationPredicateBuilder statuses(String[] statuses) {
        equal(reservation.get("status"), toEnum(statuses, Reservation.Status.class, Reservation.Status[]::new));
        return this;
    }

    private static <T extends Enum<T>> T[] toEnum(String[] strings, Class<T> clazz, IntFunction<T[]> generator) {
        return strings != null ? Arrays.stream(strings).map(string -> toEnum(string, clazz)).toArray(generator) : null;
    }

    private static <T extends Enum<T>> T toEnum(String string, Class<T> clazz) {
        try {
            return Enum.valueOf(clazz, string);
        } catch (Exception e) {
            return null;
        }
    }

    public static ReservationPredicateBuilder builder(Root<Reservation> reservation, CriteriaBuilder criteriaBuilder) {
        return new ReservationPredicateBuilder(reservation, criteriaBuilder);
    }
}
