package pl.zmudzin.library.core.domain.catalog.book;

import pl.zmudzin.library.core.domain.common.StringId;

public class BookId extends StringId {

    public BookId(String value) {
        super(value);
    }

    public static BookId of(String value) {
        return new BookId(value);
    }
}
