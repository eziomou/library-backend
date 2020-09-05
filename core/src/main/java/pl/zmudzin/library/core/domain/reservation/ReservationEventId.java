package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.common.StringId;

import java.util.UUID;

public class ReservationEventId extends StringId {

    protected ReservationEventId(String value) {
        super(value);
    }

    public static ReservationEventId of(String value) {
        return new ReservationEventId(value);
    }

    public static ReservationEventId random() {
        return new ReservationEventId(UUID.randomUUID().toString());
    }
}
