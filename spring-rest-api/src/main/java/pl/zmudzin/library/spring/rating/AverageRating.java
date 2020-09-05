package pl.zmudzin.library.spring.rating;

public class AverageRating {

    private final Double value;

    public AverageRating(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
