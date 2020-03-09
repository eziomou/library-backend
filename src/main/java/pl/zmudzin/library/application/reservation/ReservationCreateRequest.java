package pl.zmudzin.library.application.reservation;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;

/**
 * @author Piotr Żmudzin
 */
public class ReservationCreateRequest implements Serializable {

    @NotNull
    private Long bookId;

    @NotNull
    private Duration duration;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
