package pl.zmudzin.library.application.rating;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class RatingUpdateRequest implements Serializable {

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
