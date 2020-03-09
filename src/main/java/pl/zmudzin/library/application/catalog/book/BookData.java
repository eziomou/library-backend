package pl.zmudzin.library.application.catalog.book;

import org.springframework.hateoas.server.core.Relation;
import pl.zmudzin.library.application.catalog.author.AuthorBasicData;
import pl.zmudzin.library.application.catalog.genre.GenreBasicData;
import pl.zmudzin.library.application.catalog.publisher.PublisherBasicData;

import java.time.LocalDate;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "books")
public class BookData extends BookBasicData {

    private AuthorBasicData author;
    private GenreBasicData genre;
    private PublisherBasicData publisher;
    private String description;
    private LocalDate publicationDate;
    private Boolean loaned;
    private Double averageRating;

    public AuthorBasicData getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBasicData author) {
        this.author = author;
    }

    public GenreBasicData getGenre() {
        return genre;
    }

    public void setGenre(GenreBasicData genre) {
        this.genre = genre;
    }

    public PublisherBasicData getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherBasicData publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Boolean getLoaned() {
        return loaned;
    }

    public void setLoaned(Boolean loaned) {
        this.loaned = loaned;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
