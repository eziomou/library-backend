package pl.zmudzin.library.core.application.catalog.author;

import java.util.Optional;

public interface AuthorQuery {

    Optional<String> firstName();

    Optional<String> lastName();

    Optional<String> fullName();
}
