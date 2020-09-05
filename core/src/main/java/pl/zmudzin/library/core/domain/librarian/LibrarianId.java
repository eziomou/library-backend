package pl.zmudzin.library.core.domain.librarian;

import pl.zmudzin.library.core.domain.common.StringId;

public class LibrarianId extends StringId {

    public LibrarianId(String value) {
        super(value);
    }

    public static LibrarianId of(String value) {
        return new LibrarianId("");
    }
}
