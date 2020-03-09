package pl.zmudzin.library.domain.member;

import pl.zmudzin.ddd.annotations.domain.AggregateRoot;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.common.BaseEntity;
import pl.zmudzin.library.domain.common.Resource;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.rating.Rating;
import pl.zmudzin.library.domain.reservation.Reservation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@NamedEntityGraph(
        name = "member-entity-graph",
        attributeNodes = @NamedAttributeNode(value = "account")
)
@AggregateRoot
@Entity
public class Member extends BaseEntity implements Resource<Member> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "member")
    private List<Reservation> reservations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "member")
    private List<Loan> loans;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "member")
    private List<Rating> ratings;

    protected Member() {
        reservations = new ArrayList<>();
        loans = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public Member(Account account) {
        this();
        setAccount(account);
    }

    public Account getAccount() {
        return account;
    }

    private void setAccount(Account account) {
        this.account = Objects.requireNonNull(account);
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public void addReservation(Reservation reservation) {
        Objects.requireNonNull(reservation);

        if (!reservation.getMember().equals(this)) {
            throw new IllegalStateException("The reservation has a different member");
        }
        reservations.add(reservation);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public void addLoan(Loan loan) {
        Objects.requireNonNull(loan);

        if (!loan.getMember().equals(this)) {
            throw new IllegalStateException("The loan has a different member");
        }
        loans.add(loan);
    }

    public List<Rating> getRatings() {
        return Collections.unmodifiableList(ratings);
    }

    public void addRating(Rating rating) {
        Objects.requireNonNull(rating);

        if (!rating.getMember().equals(this)) {
            throw new IllegalStateException("The rating has a different member");
        }
        ratings.add(rating);
    }

    @Override
    public Member getOwner() {
        return this;
    }
}
