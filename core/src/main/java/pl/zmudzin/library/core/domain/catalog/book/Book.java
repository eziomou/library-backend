package pl.zmudzin.library.core.domain.catalog.book;

import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.time.Instant;
import java.util.Objects;

public class Book extends AbstractEntity<BookId> {

    private final String title;
    private final String description;
    private final Instant publicationDate;
    private final Author author;
    private final Genre genre;
    private final Publisher publisher;

    private Book(Builder builder) {
        super(builder.id);
        this.title = Objects.requireNonNull(builder.title);
        this.description = Objects.requireNonNull(builder.description);
        this.publicationDate = Objects.requireNonNull(builder.publicationDate);
        this.author = Objects.requireNonNull(builder.author);
        this.genre = Objects.requireNonNull(builder.genre);
        this.publisher = Objects.requireNonNull(builder.publisher);
    }

    public String getTitle() {
        return title;
    }

    public Book withTitle(String title) {
        return copyingBuilder().title(title).build();
    }

    public String getDescription() {
        return description;
    }

    public Book withDescription(String description) {
        return copyingBuilder().description(description).build();
    }

    public Instant getPublicationDate() {
        return publicationDate;
    }

    public Book withPublicationDate(Instant publicationDate) {
        return copyingBuilder().publicationDate(publicationDate).build();
    }

    public Author getAuthor() {
        return author;
    }

    public Book withAuthor(Author author) {
        return copyingBuilder().author(author).build();
    }

    public Genre getGenre() {
        return genre;
    }

    public Book withGenre(Genre genre) {
        return copyingBuilder().genre(genre).build();
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Book withPublisher(Publisher publisher) {
        return copyingBuilder().publisher(publisher).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copyingBuilder() {
        return builder()
                .id(getId())
                .title(title)
                .description(description)
                .publicationDate(publicationDate)
                .author(author)
                .genre(genre)
                .publisher(publisher);
    }

    public static class Builder {

        private BookId id;
        private String title;
        private String description;
        private Instant publicationDate;
        private Author author;
        private Genre genre;
        private Publisher publisher;

        public Builder id(BookId id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder publicationDate(Instant publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder author(Author author) {
            this.author = author;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Builder publisher(Publisher publisher) {
            this.publisher = publisher;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
