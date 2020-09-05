package pl.zmudzin.library.spring.reservation;

import pl.zmudzin.library.core.application.reservation.ReservationQuery;

import java.util.Objects;
import java.util.Optional;

public class RestReservationQuery implements ReservationQuery {

    private String status;

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<String> status() {
        return Optional.ofNullable(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestReservationQuery))
            return false;
        RestReservationQuery other = (RestReservationQuery) object;
        return Objects.equals(status, other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
