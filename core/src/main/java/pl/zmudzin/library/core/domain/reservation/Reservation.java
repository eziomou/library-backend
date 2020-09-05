package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.AbstractEntity;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Reservation extends AbstractEntity<ReservationId> {

    private final MemberId memberId;
    private final BookId bookId;
    private final List<ReservationEvent> events;

    public Reservation(ReservationId id, MemberId memberId, BookId bookId, Instant submitDate) {
        this(id, memberId, bookId, new ArrayList<>());
        submit(submitDate);
    }

    public Reservation(Builder builder) {
        this(builder.id, builder.memberId, builder.bookId, builder.events);
    }

    public Reservation(ReservationId id, MemberId memberId, BookId bookId, List<ReservationEvent> events) {
        super(id);
        this.memberId = Objects.requireNonNull(memberId);
        this.bookId = Objects.requireNonNull(bookId);
        this.events = events;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public BookId getBookId() {
        return bookId;
    }

    public List<ReservationEvent> getEvents() {
        return events.stream().sorted().collect(Collectors.toUnmodifiableList());
    }

    private Reservation withEvent(ReservationEvent event) {
        List<ReservationEvent> events = new ArrayList<>(this.events);
        events.add(event);
        return new Reservation(getId(), memberId, bookId, events);
    }

    public boolean is(ReservationStatus status) {
        return (status == null && events.size() == 0) || getStatus() == status;
    }

    public ReservationStatus getStatus() {
        return events.stream().sorted(Comparator.reverseOrder())
                .map(ReservationEvent::getStatus).findFirst().orElse(null);
    }

    private void submit(Instant date) {
        if (is(null)) {
            events.add(new ReservationEvent(ReservationEventId.random(), ReservationStatus.SUBMITTED, date));
        } else {
            throw newInvalidStatusException();
        }
    }

    public Reservation prepare(Instant date) {
        if (is(ReservationStatus.SUBMITTED)) {
            return withEvent(new ReservationEvent(ReservationEventId.random(), ReservationStatus.PREPARED, date));
        } else {
            throw newInvalidStatusException();
        }
    }

    public Reservation realize(Instant date) {
        if (is(ReservationStatus.PREPARED)) {
            return withEvent(new ReservationEvent(ReservationEventId.random(), ReservationStatus.REALIZED, date));
        } else {
            throw newInvalidStatusException();
        }
    }

    public Reservation cancel(Instant date) {
        if (is(null) || is(ReservationStatus.SUBMITTED) || is(ReservationStatus.PREPARED)) {
            return withEvent(new ReservationEvent(ReservationEventId.random(), ReservationStatus.CANCELLED, date));
        } else {
            throw newInvalidStatusException();
        }
    }

    public Reservation reject(Instant date) {
        if (is(null) || is(ReservationStatus.SUBMITTED) || is(ReservationStatus.PREPARED)) {
            return withEvent(new ReservationEvent(ReservationEventId.random(), ReservationStatus.REJECTED, date));
        } else {
            throw newInvalidStatusException();
        }
    }

    private RuntimeException newInvalidStatusException() {
        return new IllegalStateException("Invalid status: " + getStatus());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<ReservationEvent> events = new ArrayList<>();

        private ReservationId id;
        private MemberId memberId;
        private BookId bookId;

        private Builder() {
        }

        public Builder id(ReservationId id) {
            this.id = id;
            return this;
        }

        public Builder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder bookId(BookId bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder event(ReservationEvent event) {
            events.add(event);
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}