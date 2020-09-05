package pl.zmudzin.library.spring.catalog.book;

import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.application.catalog.book.BookQuery;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class RestBookQuery implements BookQuery {

    private String phrase;
    private String title;
    private String description;
    private String minPublicationDate;
    private String maxPublicationDate;
    private String authorId;
    private String genreId;
    private String publisherId;

    @Override
    public Optional<String> phrase() {
        return Optional.ofNullable(phrase);
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Optional<Instant> getMinPublicationDate() {
        return Optional.ofNullable(maxPublicationDate != null ? Instant.parse(minPublicationDate) : null);
    }

    public void setMinPublicationDate(String minPublicationDate) {
        this.minPublicationDate = minPublicationDate;
    }

    @Override
    public Optional<Instant> getMaxPublicationDate() {
        return Optional.ofNullable(maxPublicationDate != null ? Instant.parse(maxPublicationDate) : null);
    }

    public void setMaxPublicationDate(String maxPublicationDate) {
        this.maxPublicationDate = maxPublicationDate;
    }

    @Override
    public Optional<AuthorId> getAuthorId() {
        return Optional.ofNullable(authorId != null ? AuthorId.of(authorId) : null);
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public Optional<GenreId> getGenreId() {
        return Optional.ofNullable(genreId != null ? GenreId.of(genreId) : null);
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public Optional<PublisherId> getPublisherId() {
        return Optional.ofNullable(publisherId != null ? PublisherId.of(publisherId) : null);
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestBookQuery))
            return false;
        RestBookQuery that = (RestBookQuery) object;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(minPublicationDate, that.minPublicationDate) &&
                Objects.equals(maxPublicationDate, that.maxPublicationDate) &&
                Objects.equals(authorId, that.authorId) &&
                Objects.equals(genreId, that.genreId) &&
                Objects.equals(publisherId, that.publisherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, minPublicationDate, maxPublicationDate, authorId, genreId, publisherId);
    }
}
