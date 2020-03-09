package pl.zmudzin.library.application.reservation;

import pl.zmudzin.library.domain.reservation.Reservation;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author Piotr Żmudzin
 */
public class ReservationUpdateRequest implements Serializable {

    private Reservation.Status status;
    private Duration duration;

    public Reservation.Status getStatus() {
        return status;
    }

    public void setStatus(Reservation.Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
