package pl.zmudzin.library.application.rating;

import org.springframework.hateoas.server.core.Relation;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "ratings")
public class RatingData implements Serializable {

    private Long id;
    private MemberBasicData member;
    private BookBasicData book;
    private int value;
    private LocalDateTime rateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberBasicData getMember() {
        return member;
    }

    public void setMember(MemberBasicData member) {
        this.member = member;
    }

    public BookBasicData getBook() {
        return book;
    }

    public void setBook(BookBasicData book) {
        this.book = book;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDateTime rateDate) {
        this.rateDate = rateDate;
    }
}
