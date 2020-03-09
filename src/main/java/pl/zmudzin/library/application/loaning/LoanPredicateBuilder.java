package pl.zmudzin.library.application.loaning;

import pl.zmudzin.library.application.util.PersonPredicateBuilder;
import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.member.Member;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Custom predicate builder to avoid duplication of joins.
 *
 * @author Piotr Żmudzin
 */
public class LoanPredicateBuilder extends PredicateBuilder {

    private Root<Loan> loan;
    private Join<Loan, Member> member;
    private Join<Loan, Account> account;
    private Join<Loan, Book> book;

    private LoanPredicateBuilder(Root<Loan> loan, CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.loan = loan;
    }

    private Join<Loan, Member> getMember() {
        if (member == null) {
            member = loan.join("member", JoinType.LEFT);
        }
        return member;
    }

    private Join<Loan, Account> getAccount() {
        if (account == null) {
            account = getMember().join("account", JoinType.LEFT);
        }
        return account;
    }

    private Join<Loan, Book> getBook() {
        if (book == null) {
            book = loan.join("book", JoinType.LEFT);
        }
        return book;
    }

    public LoanPredicateBuilder memberUsername(String username) {
        equal(getAccount().get("username"), username);
        return this;
    }

    public LoanPredicateBuilder memberFullName(String fullName) {
        like(PersonPredicateBuilder.getFullName(getAccount().get("profile"), criteriaBuilder), fullName);
        return this;
    }

    public LoanPredicateBuilder bookId(Long id) {
        equal(getBook().get("id"), id);
        return this;
    }

    public LoanPredicateBuilder bookTitle(String title) {
        like(getBook().get("title"), title);
        return this;
    }

    public LoanPredicateBuilder completed(Boolean completed) {
        equal(loan.get("completed"), completed);
        return this;
    }

    public static LoanPredicateBuilder builder(Root<Loan> reservation, CriteriaBuilder criteriaBuilder) {
        return new LoanPredicateBuilder(reservation, criteriaBuilder);
    }
}
