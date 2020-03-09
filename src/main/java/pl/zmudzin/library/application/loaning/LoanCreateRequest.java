package pl.zmudzin.library.application.loaning;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class LoanCreateRequest implements Serializable {

    @NotNull
    private Long reservationId;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
