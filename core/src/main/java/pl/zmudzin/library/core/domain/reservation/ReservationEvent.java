package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.time.Instant;
import java.util.Objects;

public class ReservationEvent extends AbstractEntity<ReservationEventId> implements Comparable<ReservationEvent> {

    private final ReservationStatus status;
    private final Instant occurrenceDate;

    public ReservationEvent(ReservationEventId id, ReservationStatus status, Instant occurrenceDate) {
        super(id);
        this.status = Objects.requireNonNull(status);
        this.occurrenceDate = Objects.requireNonNull(occurrenceDate);
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Instant getOccurrenceDate() {
        return occurrenceDate;
    }

    @Override
    public int compareTo(ReservationEvent other) {
        return occurrenceDate.compareTo(other.occurrenceDate);
    }
}
