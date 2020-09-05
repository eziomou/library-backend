package pl.zmudzin.library.core.domain.catalog.genre;

import pl.zmudzin.library.core.domain.common.StringId;

public class GenreId extends StringId {

    public GenreId(String value) {
        super(value);
    }

    public static GenreId of(String value) {
        return new GenreId(value);
    }
}
