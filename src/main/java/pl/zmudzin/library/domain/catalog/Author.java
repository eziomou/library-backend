package pl.zmudzin.library.domain.catalog;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;

/**
 * @author Piotr Żmudzin
 */
@AggregateRoot
@Entity
public class Author extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Book> books;

    @SuppressWarnings("unused")
    protected Author() {
        books = new HashSet<>();
    }

    public Author(String firstName, String lastName) {
        this();
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Set<Book> getBooks() {
        return Collections.unmodifiableSet(books);
    }

    public void addBook(Book book) {
        Objects.requireNonNull(book);

        if (!book.getAuthor().equals(this)) {
            throw new IllegalArgumentException("The book has a different author");
        }
        books.add(book);
    }

    public void removeBook(Book book) {
        Objects.requireNonNull(book);

        if (!books.contains(book)) {
            throw new IllegalArgumentException("The author doesn't have this book");
        }
        books.remove(book);
    }
}
