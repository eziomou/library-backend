package pl.zmudzin.library.core.application.catalog.book;

import pl.zmudzin.library.core.application.catalog.author.AuthorData;
import pl.zmudzin.library.core.application.catalog.genre.GenreData;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherData;

import java.util.function.Consumer;

public final class BookData extends SimpleBookData {

    private final String description;
    private final String publicationDate;
    private final AuthorData author;
    private final GenreData genre;
    private final PublisherData publisher;

    private BookData(Builder builder) {
        super(builder.id, builder.title);
        this.description = builder.description;
        this.publicationDate = builder.publicationDate;
        this.author = builder.author;
        this.genre = builder.genre;
        this.publisher = builder.publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public AuthorData getAuthor() {
        return author;
    }

    public GenreData getGenre() {
        return genre;
    }

    public PublisherData getPublisher() {
        return publisher;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SimpleBookData.Builder<Builder> {

        private String description;
        private String publicationDate;
        private AuthorData author;
        private GenreData genre;
        private PublisherData publisher;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder publicationDate(String publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder author(Consumer<AuthorData.Builder> consumer) {
            AuthorData.Builder builder = AuthorData.builder();
            consumer.accept(builder);
            this.author = builder.build();
            return this;
        }

        public Builder genre(Consumer<GenreData.Builder> consumer) {
            GenreData.Builder builder = GenreData.builder();
            consumer.accept(builder);
            this.genre = builder.build();
            return this;
        }

        public Builder publisher(Consumer<PublisherData.Builder> consumer) {
            PublisherData.Builder builder = PublisherData.builder();
            consumer.accept(builder);
            this.publisher = builder.build();
            return this;
        }

        public BookData build() {
            return new BookData(this);
        }
    }
}
