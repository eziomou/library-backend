package pl.zmudzin.library.core.domain.catalog.author;

import pl.zmudzin.library.core.domain.common.StringId;

public class AuthorId extends StringId {

    public AuthorId(String value) {
        super(value);
    }

    public static AuthorId of(String value) {
        return new AuthorId(value);
    }
}
