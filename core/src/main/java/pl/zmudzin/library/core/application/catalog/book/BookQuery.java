package pl.zmudzin.library.core.application.catalog.book;

import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;

import java.time.Instant;
import java.util.Optional;

public interface BookQuery {

    Optional<String> phrase();

    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<Instant> getMinPublicationDate();

    Optional<Instant> getMaxPublicationDate();

    Optional<AuthorId> getAuthorId();

    Optional<GenreId> getGenreId();

    Optional<PublisherId> getPublisherId();
}
