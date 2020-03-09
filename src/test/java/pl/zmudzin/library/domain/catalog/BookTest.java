package pl.zmudzin.library.domain.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.util.ReflectionUtil;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
class BookTest {

    private static final String TITLE = "title";
    private static final Author AUTHOR = Mockito.mock(Author.class);
    private static final Genre GENRE = Mockito.mock(Genre.class);
    private static final Publisher PUBLISHER = Mockito.mock(Publisher.class);
    private static final String DESCRIPTION = "description";
    private static final LocalDate PUBLICATION_DATE = LocalDate.now();

    private Book book;

    @BeforeEach
    void beforeEach() {
        book = new Book(TITLE, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE);
        ReflectionUtil.setField(book, "id", 1L);
    }

    @Test
    void constructor_nullTitle_throwsException() {
        assertThrows(NullPointerException.class,
                () -> new Book(null, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE));
    }

    @Test
    void constructor_nullAuthor_throwsException() {
        assertThrows(NullPointerException.class,
                () -> new Book(TITLE, null, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE));
    }

    @Test
    void constructor_nullGenre_throwsException() {
        assertThrows(NullPointerException.class,
                () -> new Book(TITLE, AUTHOR, null, PUBLISHER, DESCRIPTION, PUBLICATION_DATE));
    }

    @Test
    void constructor_nullPublisher_throwsException() {
        assertThrows(NullPointerException.class,
                () -> new Book(TITLE, AUTHOR, GENRE, null, DESCRIPTION, PUBLICATION_DATE));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Book(TITLE, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE));
    }

    @Test
    void addReservation_nullReservation_throwsException() {
        assertThrows(NullPointerException.class, () -> book.addReservation(null));
    }

    @Test
    void addReservation_hasDifferentBook_throwsException() {
        Book otherBook = new Book(TITLE, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE);
        ReflectionUtil.setField(otherBook, "id", book.getId() + 1L);

        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservation.getBook()).thenReturn(otherBook);

        assertThrows(IllegalStateException.class, () -> book.addReservation(reservation));
    }

    @Test
    void addReservation_validReservation_addsReservation() {
        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservation.getBook()).thenReturn(book);

        book.addReservation(reservation);

        assertTrue(book.getReservations().contains(reservation));
    }

    @Test
    void addLoan_nullLoan_throwsException() {
        assertThrows(NullPointerException.class, () -> book.addLoan(null));
    }

    @Test
    void addLoan_hasDifferentBook_throwsException() {
        Book otherBook = new Book(TITLE, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE);
        ReflectionUtil.setField(otherBook, "id", book.getId() + 1L);

        Loan loan = Mockito.mock(Loan.class);
        when(loan.getBook()).thenReturn(otherBook);

        assertThrows(IllegalArgumentException.class, () -> book.addLoan(loan));
    }

    @Test
    void addLoan_validLoan_addsLoan() {
        Loan loan = Mockito.mock(Loan.class);
        when(loan.getBook()).thenReturn(book);

        book.addLoan(loan);

        assertTrue(book.getLoans().contains(loan));
    }

    @Test
    void addRating_nullRating_throwsException() {
        assertThrows(NullPointerException.class, () -> book.addReservation(null));
    }

    @Test
    void addRating_hasDifferentBook_throwsException() {
        Book otherBook = new Book(TITLE, AUTHOR, GENRE, PUBLISHER, DESCRIPTION, PUBLICATION_DATE);
        ReflectionUtil.setField(otherBook, "id", book.getId() + 1L);

        Rating rating = Mockito.mock(Rating.class);
        when(rating.getBook()).thenReturn(otherBook);

        assertThrows(IllegalArgumentException.class, () -> book.addRating(rating));
    }

    @Test
    void addRating_validRating_addsRating() {
        Rating rating = Mockito.mock(Rating.class);
        when(rating.getBook()).thenReturn(book);

        book.addRating(rating);

        assertTrue(book.getRatings().contains(rating));
    }
}