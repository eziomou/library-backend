package pl.zmudzin.library.core.domain.reservation;

import pl.zmudzin.library.core.domain.common.StringId;

public class ReservationId extends StringId {

    public ReservationId(String value) {
        super(value);
    }

    public static ReservationId of(String value) {
        return new ReservationId(value);
    }
}
