/*
 * This file is generated by jOOQ.
 */
package pl.zmudzin.library.persistence.jooq.schema;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import pl.zmudzin.library.persistence.jooq.schema.tables.Account;
import pl.zmudzin.library.persistence.jooq.schema.tables.Author;
import pl.zmudzin.library.persistence.jooq.schema.tables.Book;
import pl.zmudzin.library.persistence.jooq.schema.tables.Genre;
import pl.zmudzin.library.persistence.jooq.schema.tables.Librarian;
import pl.zmudzin.library.persistence.jooq.schema.tables.Loan;
import pl.zmudzin.library.persistence.jooq.schema.tables.Member;
import pl.zmudzin.library.persistence.jooq.schema.tables.Publisher;
import pl.zmudzin.library.persistence.jooq.schema.tables.Rating;
import pl.zmudzin.library.persistence.jooq.schema.tables.Reservation;
import pl.zmudzin.library.persistence.jooq.schema.tables.ReservationEvent;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 946494612;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.account</code>.
     */
    public final Account ACCOUNT = Account.ACCOUNT;

    /**
     * The table <code>public.author</code>.
     */
    public final Author AUTHOR = Author.AUTHOR;

    /**
     * The table <code>public.book</code>.
     */
    public final Book BOOK = Book.BOOK;

    /**
     * The table <code>public.genre</code>.
     */
    public final Genre GENRE = Genre.GENRE;

    /**
     * The table <code>public.librarian</code>.
     */
    public final Librarian LIBRARIAN = Librarian.LIBRARIAN;

    /**
     * The table <code>public.loan</code>.
     */
    public final Loan LOAN = Loan.LOAN;

    /**
     * The table <code>public.member</code>.
     */
    public final Member MEMBER = Member.MEMBER;

    /**
     * The table <code>public.publisher</code>.
     */
    public final Publisher PUBLISHER = Publisher.PUBLISHER;

    /**
     * The table <code>public.rating</code>.
     */
    public final Rating RATING = Rating.RATING;

    /**
     * The table <code>public.reservation</code>.
     */
    public final Reservation RESERVATION = Reservation.RESERVATION;

    /**
     * The table <code>public.reservation_event</code>.
     */
    public final ReservationEvent RESERVATION_EVENT = ReservationEvent.RESERVATION_EVENT;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Account.ACCOUNT,
            Author.AUTHOR,
            Book.BOOK,
            Genre.GENRE,
            Librarian.LIBRARIAN,
            Loan.LOAN,
            Member.MEMBER,
            Publisher.PUBLISHER,
            Rating.RATING,
            Reservation.RESERVATION,
            ReservationEvent.RESERVATION_EVENT);
    }
}
