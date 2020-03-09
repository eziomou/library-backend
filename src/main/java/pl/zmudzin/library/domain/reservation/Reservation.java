package pl.zmudzin.library.domain.reservation;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.common.BaseEntity;
import pl.zmudzin.library.domain.common.Resource;
import pl.zmudzin.library.domain.member.Member;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Piotr Żmudzin
 */
@NamedEntityGraph(
        name = "reservation-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "member", subgraph = "reservation-member-entity-graph"),
                @NamedAttributeNode(value = "book")
        },
        subgraphs = @NamedSubgraph(name = "reservation-member-entity-graph", attributeNodes = @NamedAttributeNode(value = "account"))
)
@AggregateRoot
@Entity
public class Reservation extends BaseEntity implements Resource<Member> {

    public static final Duration MIN_DURATION = Duration.ofDays(1);
    public static final Duration MAX_DURATION = Duration.ofDays(28);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    @Column(nullable = false)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submitDate;

    private LocalDateTime prepareDate;

    private LocalDateTime completeDate;

    @Column(insertable = false)
    private LocalDateTime cancelDate;

    @Column(insertable = false)
    private LocalDateTime rejectDate;

    @SuppressWarnings("unused")
    protected Reservation() {
    }

    Reservation(Member member, Book book, Duration duration, LocalDateTime submitDate) {
        setMember(member);
        setBook(book);
        setDuration(duration);
        submit(submitDate);
    }

    public Member getMember() {
        return member;
    }

    private void setMember(Member member) {
        this.member = Objects.requireNonNull(member);

        if (!member.getReservations().contains(this)) {
            member.addReservation(this);
        }
    }

    public Book getBook() {
        return book;
    }

    private void setBook(Book book) {
        this.book = Objects.requireNonNull(book);

        if (!book.getReservations().contains(this)) {
            book.addReservation(this);
        }
    }

    public Duration getDuration() {
        return duration;
    }

    private void setDuration(Duration duration) {
        Objects.requireNonNull(duration);

        if (duration.compareTo(MIN_DURATION) < 0 || duration.compareTo(MAX_DURATION) > 0) {
            throw new IllegalArgumentException("Duration must be between " + MIN_DURATION + " and " + MAX_DURATION);
        }
        this.duration = duration;
    }

    public void updateDuration(Duration duration) {
        if (status != Status.SUBMITTED && status != Status.PREPARED) {
            throw new IllegalStateException("Status must be " + Status.SUBMITTED + " or " + Status.PREPARED);
        }
        setDuration(duration);
    }

    public Status getStatus() {
        return status;
    }

    public void submit(LocalDateTime submitDate) {
        this.submitDate = Objects.requireNonNull(submitDate);

        if (status != null) {
            throw new IllegalStateException("Status must be null");
        }
        status = Status.SUBMITTED;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void prepare(LocalDateTime prepareDate) {
        this.prepareDate = Objects.requireNonNull(prepareDate);

        if (status != Status.SUBMITTED) {
            throw new IllegalStateException("Status must be " + Status.SUBMITTED);
        }
        status = Status.PREPARED;
    }

    public LocalDateTime getPrepareDate() {
        return prepareDate;
    }

    public void complete(LocalDateTime completeDate) {
        this.completeDate = Objects.requireNonNull(completeDate);

        if (status != Status.PREPARED) {
            throw new IllegalStateException("Status must be " + Status.PREPARED);
        }
        status = Status.COMPLETED;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void cancel(LocalDateTime cancelDate) {
        this.cancelDate = Objects.requireNonNull(cancelDate);

        if (status != Status.SUBMITTED && status != Status.PREPARED) {
            throw new IllegalStateException("Status must be " + Status.SUBMITTED + " or " + Status.PREPARED);
        }
        status = Status.CANCELLED;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void reject(LocalDateTime rejectDate) {
        this.rejectDate = Objects.requireNonNull(rejectDate);

        if (status != Status.SUBMITTED && status != Status.PREPARED) {
            throw new IllegalStateException("Status must be " + Status.SUBMITTED + " or " + Status.PREPARED);
        }
        status = Status.REJECTED;
    }

    public LocalDateTime getRejectDate() {
        return rejectDate;
    }

    public int getQueuePosition() {
        int position = book.getReservations().stream()
                .filter(r -> r.getStatus() == Status.SUBMITTED || r.getStatus() == Status.PREPARED)
                .collect(Collectors.toList())
                .indexOf(this);

        return ++position;
    }

    @Override
    public Member getOwner() {
        return member;
    }

    public enum Status {
        SUBMITTED,
        PREPARED,
        COMPLETED,
        CANCELLED,
        REJECTED
    }
}
