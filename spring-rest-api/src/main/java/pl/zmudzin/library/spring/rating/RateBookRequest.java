package pl.zmudzin.library.spring.rating;

import javax.validation.constraints.NotNull;

public final class RateBookRequest {

    @NotNull
    private int value;

    public RateBookRequest() {
    }

    public RateBookRequest(@NotNull int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
