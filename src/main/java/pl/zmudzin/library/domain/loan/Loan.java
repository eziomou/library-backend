package pl.zmudzin.library.domain.loan;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.common.BaseEntity;
import pl.zmudzin.library.domain.common.Resource;
import pl.zmudzin.library.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@NamedEntityGraph(
        name = "loan-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "member", subgraph = "loan-member-entity-graph"),
                @NamedAttributeNode(value = "book")
        },
        subgraphs = @NamedSubgraph(name = "loan-member-entity-graph", attributeNodes = @NamedAttributeNode(value = "account"))
)
@AggregateRoot
@Entity
public class Loan extends BaseEntity implements Resource<Member> {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Book book;

    private LocalDateTime loanDate;

    private LocalDateTime dueDate;

    private boolean completed;

    private LocalDateTime completeDate;

    @SuppressWarnings("unused")
    protected Loan() {
    }

    Loan(Member member, Book book, LocalDateTime loanDate, LocalDateTime dueDate) {
        setMember(member);
        setBook(book);
        setLoanDate(loanDate);
        setDueDate(dueDate);
    }

    public Member getMember() {
        return member;
    }

    private void setMember(Member member) {
        this.member = Objects.requireNonNull(member);

        if (!member.getLoans().contains(this)) {
            member.addLoan(this);
        }
    }

    public Book getBook() {
        return book;
    }

    private void setBook(Book book) {
        this.book = Objects.requireNonNull(book);

        if (!book.getLoans().contains(this)) {
            book.addLoan(this);
        }
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    private void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = Objects.requireNonNull(loanDate);
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    private void setDueDate(LocalDateTime dueDate) {
        this.dueDate = Objects.requireNonNull(dueDate);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete(LocalDateTime completeDate) {
        this.completeDate = Objects.requireNonNull(completeDate);

        if (completed) {
            throw new IllegalStateException("The loan is already completed");
        }
        completed = true;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    @Override
    public Member getOwner() {
        return member;
    }
}
