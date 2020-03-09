package pl.zmudzin.library.domain.catalog;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Piotr Żmudzin
 */
@AggregateRoot
@Entity
public class Publisher extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    private Set<Book> books;

    @SuppressWarnings("unused")
    protected Publisher() {
        books = new HashSet<>();
    }

    public Publisher(String name) {
        this();
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Set<Book> getBooks() {
        return Collections.unmodifiableSet(books);
    }

    public void addBook(Book book) {
        Objects.requireNonNull(book);

        if (!book.getPublisher().equals(this)) {
            throw new IllegalArgumentException("The book has a different publisher");
        }
        books.add(book);
    }

    public void removeBook(Book book) {
        Objects.requireNonNull(book);

        if (!books.contains(book)) {
            throw new IllegalArgumentException("The publisher doesn't have this book");
        }
        books.remove(book);
    }
}
