package pl.zmudzin.library.application.loaning;

import org.springframework.hateoas.server.core.Relation;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  @author Piotr Żmudzin
 */
@Relation(collectionRelation = "loans")
public class LoanData implements Serializable {

    private Long id;
    private MemberBasicData member;
    private BookBasicData book;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private boolean completed;
    private LocalDateTime completeDate;

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

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }
}
