package pl.zmudzin.library.application.catalog.book;

import pl.zmudzin.library.application.util.PersonPredicateBuilder;
import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.catalog.Author;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.catalog.Genre;
import pl.zmudzin.library.domain.catalog.Publisher;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * Custom predicate builder to avoid duplication of joins.
 *
 * @author Piotr Żmudzin
 */
public class BookPredicateBuilder extends PredicateBuilder {

    private Root<Book> book;
    private Join<Book, Author> author;
    private Join<Book, Genre> genre;
    private Join<Book, Publisher> publisher;

    private BookPredicateBuilder(Root<Book> book, CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.book = book;
    }

    private Join<Book, Author> getAuthor() {
        if (author == null) {
            author = book.join("author", JoinType.LEFT);
        }
        return author;
    }

    private Join<Book, Genre> getGenre() {
        if (genre == null) {
            genre = book.join("genre", JoinType.LEFT);
        }
        return genre;
    }

    private Join<Book, Publisher> getPublisher() {
        if (publisher == null) {
            publisher = book.join("publisher", JoinType.LEFT);
        }
        return publisher;
    }

    public BookPredicateBuilder title(String title) {
        like(book.get("title"), title);
        return this;
    }

    public BookPredicateBuilder authorId(Long id) {
        equal(getAuthor().get("id"), id);
        return this;
    }

    public BookPredicateBuilder authorFullName(String authorFullName) {
        like(PersonPredicateBuilder.getFullName(author, criteriaBuilder), authorFullName);
        return this;
    }

    public BookPredicateBuilder genreId(Long id) {
        equal(getGenre().get("id"), id);
        return this;
    }

    public BookPredicateBuilder genreName(String name) {
        like(getGenre().get("name"), name);
        return this;
    }

    public BookPredicateBuilder publisherId(Long id) {
        equal(getPublisher().get("id"), id);
        return this;
    }

    public BookPredicateBuilder publisherName(String name) {
        like(getPublisher().get("name"), name);
        return this;
    }

    public static BookPredicateBuilder builder(Root<Book> book, CriteriaBuilder criteriaBuilder) {
        return new BookPredicateBuilder(book, criteriaBuilder);
    }
}
