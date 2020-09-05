package pl.zmudzin.library.core.domain.rating;

import pl.zmudzin.library.core.domain.common.StringId;

public class RatingId extends StringId {

    public RatingId(String value) {
        super(value);
    }

    public static RatingId of(String value) {
        return new RatingId(value);
    }
}
