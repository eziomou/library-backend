package pl.zmudzin.library.application.reservation;

import org.springframework.hateoas.server.core.Relation;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;
import pl.zmudzin.library.domain.reservation.Reservation;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "reservations")
public class ReservationData implements Serializable {

    private Long id;
    private MemberBasicData member;
    private BookBasicData book;
    private Duration duration;
    private Reservation.Status status;
    private LocalDateTime submitDate;
    private LocalDateTime prepareDate;
    private LocalDateTime completeDate;
    private LocalDateTime cancelDate;
    private LocalDateTime rejectDate;
    private int queuePosition;

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Reservation.Status getStatus() {
        return status;
    }

    public void setStatus(Reservation.Status status) {
        this.status = status;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public LocalDateTime getPrepareDate() {
        return prepareDate;
    }

    public void setPrepareDate(LocalDateTime prepareDate) {
        this.prepareDate = prepareDate;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public LocalDateTime getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(LocalDateTime rejectDate) {
        this.rejectDate = rejectDate;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }
}
