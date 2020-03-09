package pl.zmudzin.library.domain.rating;

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
        name = "rating-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "member", subgraph = "rating-member-entity-graph"),
                @NamedAttributeNode(value = "book")
        },
        subgraphs = @NamedSubgraph(name = "rating-member-entity-graph", attributeNodes = @NamedAttributeNode(value = "account"))
)
@AggregateRoot
@Entity
public class Rating extends BaseEntity implements Resource<Member> {

    static final int MIN_VALUE = 1;
    static final int MAX_VALUE = 5;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Book book;

    @Column(nullable = false)
    private int value;

    private LocalDateTime rateDate;

    @SuppressWarnings("unused")
    protected Rating() {
    }

    Rating(Member member, Book book, int value, LocalDateTime rateDate) {
        setValue(value);
        setMember(member);
        setBook(book);
        setRateDate(rateDate);
    }

    public Member getMember() {
        return member;
    }

    private void setMember(Member member) {
        this.member = Objects.requireNonNull(member);

        if (!member.getRatings().contains(this)) {
            member.addRating(this);
        }
    }

    public Book getBook() {
        return book;
    }

    private void setBook(Book book) {
        this.book = Objects.requireNonNull(book);

        if (!book.getRatings().contains(this)) {
            book.addRating(this);
        }
    }

    public int getValue() {
        return value;
    }

    private void setValue(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between " + MIN_VALUE + " and " + MAX_VALUE + ".");
        }
        this.value = value;
    }

    public void updateValue(int value) {
        book.removeRating(this);
        setValue(value);
        book.addRating(this);
    }

    public LocalDateTime getRateDate() {
        return rateDate;
    }

    private void setRateDate(LocalDateTime rateDate) {
        this.rateDate = Objects.requireNonNull(rateDate);
    }

    @Override
    public Member getOwner() {
        return member;
    }
}
