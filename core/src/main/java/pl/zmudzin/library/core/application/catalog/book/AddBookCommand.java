package pl.zmudzin.library.core.application.catalog.book;

public final class AddBookCommand {

    private final String title;
    private final String description;
    private final String publicationDate;
    private final String authorId;
    private final String genreId;
    private final String publisherId;

    private AddBookCommand(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.publicationDate = builder.publicationDate;
        this.authorId = builder.authorId;
        this.genreId = builder.genreId;
        this.publisherId = builder.publisherId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getGenreId() {
        return genreId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String title;
        private String description;
        private String publicationDate;
        private String authorId;
        private String genreId;
        private String publisherId;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder publicationDate(String publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder authorId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder genreId(String genreId) {
            this.genreId = genreId;
            return this;
        }

        public Builder publisherId(String publisherId) {
            this.publisherId = publisherId;
            return this;
        }

        public AddBookCommand build() {
            return new AddBookCommand(this);
        }
    }
}
