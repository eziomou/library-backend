package pl.zmudzin.library.application.loaning;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class LoanUpdateRequest implements Serializable {

    private Boolean completed;

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
