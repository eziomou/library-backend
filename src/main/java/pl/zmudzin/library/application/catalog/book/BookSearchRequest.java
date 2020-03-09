package pl.zmudzin.library.application.catalog.book;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class BookSearchRequest implements Serializable {

    private String title;
    private Long authorId;
    private String authorFullName;
    private Long genreId;
    private String genreName;
    private Long publisherId;
    private String publisherName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BookSearchRequest)) {
            return false;
        }
        BookSearchRequest other = (BookSearchRequest) obj;

        return Objects.equals(title, other.title) &&
                Objects.equals(authorId, other.authorId) &&
                Objects.equals(authorFullName, other.authorFullName) &&
                Objects.equals(genreId, other.genreId) &&
                Objects.equals(genreName, other.genreName) &&
                Objects.equals(publisherId, other.publisherId) &&
                Objects.equals(publisherName, other.publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorId, authorFullName, genreId, genreName, publisherId, publisherName);
    }
}
