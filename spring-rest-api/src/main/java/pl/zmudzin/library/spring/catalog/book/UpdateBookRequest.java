package pl.zmudzin.library.spring.catalog.book;

import javax.validation.constraints.NotNull;

public class UpdateBookRequest {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String publicationDate;

    @NotNull
    private String authorId;

    @NotNull
    private String genreId;

    @NotNull
    private String publisherId;

    public UpdateBookRequest() {
    }

    private UpdateBookRequest(Builder builder) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
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

        public UpdateBookRequest build() {
            return new UpdateBookRequest(this);
        }
    }
}
