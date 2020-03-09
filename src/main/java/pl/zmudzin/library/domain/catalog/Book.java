package pl.zmudzin.library.domain.catalog;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.common.BaseEntity;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.reservation.Reservation;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Piotr Żmudzin
 */
@NamedEntityGraph(
        name = "book-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "author"),
                @NamedAttributeNode(value = "genre"),
                @NamedAttributeNode(value = "publisher")
        }
)
@AggregateRoot
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Publisher publisher;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate publicationDate;

    private boolean loaned;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @OrderColumn(name = "index")
    private List<Reservation> reservations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<Loan> loans;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<Rating> ratings;

    private double averageRating = 0.; // Additional field to increase performance

    protected Book() {
        reservations = new ArrayList<>();
        loans = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public Book(String title, Author author, Genre genre, Publisher publisher, String description,
                LocalDate publicationDate) {
        this();
        setTitle(title);
        setAuthor(author);
        setGenre(genre);
        setPublisher(publisher);
        setDescription(description);
        setPublicationDate(publicationDate);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public Author getAuthor() {
        return author;
    }

    private void setAuthor(Author author) {
        this.author = Objects.requireNonNull(author);

        if (!author.getBooks().contains(this)) {
            author.addBook(this);
        }
    }

    public void changeAuthor(Author author) {
        Objects.requireNonNull(author);
        this.author.removeBook(this);
        setAuthor(author);
    }

    public Genre getGenre() {
        return genre;
    }

    private void setGenre(Genre genre) {
        this.genre = Objects.requireNonNull(genre);

        if (!genre.getBooks().contains(this)) {
            genre.addBook(this);
        }
    }

    public void changeGenre(Genre genre) {
        Objects.requireNonNull(genre);
        this.genre.removeBook(this);
        setGenre(genre);
    }

    public Publisher getPublisher() {
        return publisher;
    }

    private void setPublisher(Publisher publisher) {
        this.publisher = Objects.requireNonNull(publisher);

        if (!publisher.getBooks().contains(this)) {
            publisher.addBook(this);
        }
    }

    public void changePublisher(Publisher publisher) {
        Objects.requireNonNull(publisher);
        this.publisher.removeBook(this);
        setPublisher(publisher);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = Objects.requireNonNull(publicationDate);
    }

    public boolean isLoaned() {
        return loaned;
    }

    public void setLoaned(boolean loaned) {
        this.loaned = loaned;
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public List<Reservation> getReservations(Reservation.Status status) {
        return reservations.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toUnmodifiableList());
    }

    public void addReservation(Reservation reservation) {
        Objects.requireNonNull(reservation);

        if (!reservation.getBook().equals(this)) {
            throw new IllegalStateException("The reservation has a different book");
        }
        reservations.add(reservation);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public void addLoan(Loan loan) {
        Objects.requireNonNull(loan);

        if (!loan.getBook().equals(this)) {
            throw new IllegalArgumentException("The loan has a different book");
        }
        loans.add(loan);
    }

    public List<Rating> getRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public void addRating(Rating rating) {
        Objects.requireNonNull(rating);

        if (!rating.getBook().equals(this)) {
            throw new IllegalArgumentException("The rating has a different book");
        }
        averageRating = (averageRating * ratings.size() + rating.getValue()) / (ratings.size() + 1);
        ratings.add(rating);
    }

    public void removeRating(Rating rating) {
        if (!ratings.contains(rating)) {
            throw new IllegalArgumentException("The book doesn't contain this rating");
        }
        if (ratings.size() > 1) {
            averageRating = (averageRating * ratings.size() - rating.getValue()) / (ratings.size() - 1);
        } else {
            averageRating = 0.;
        }
        ratings.remove(rating);
    }

    public double getAverageRating() {
        return averageRating;
    }
}
