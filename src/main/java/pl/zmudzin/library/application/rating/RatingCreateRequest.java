package pl.zmudzin.library.application.rating;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class RatingCreateRequest implements Serializable {

    @NotNull
    private Long bookId;

    @NotNull
    private int value;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
