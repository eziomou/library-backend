package pl.zmudzin.library.core.domain.loan;

import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.common.AbstractEntity;
import pl.zmudzin.library.core.domain.member.MemberId;

import java.time.Instant;
import java.util.Objects;

public class Loan extends AbstractEntity<LoanId> {

    private final MemberId memberId;
    private final BookId bookId;
    private final Instant loanDate;
    private final Instant dueDate;
    private final boolean returned;

    private Loan(Builder builder) {
        super(builder.id);
        this.memberId = Objects.requireNonNull(builder.memberId);
        this.bookId = Objects.requireNonNull(builder.bookId);
        this.loanDate = Objects.requireNonNull(builder.loanDate);
        this.dueDate = Objects.requireNonNull(builder.dueDate);
        this.returned = builder.returned;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public BookId getBookId() {
        return bookId;
    }

    public Instant getLoanDate() {
        return loanDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public Loan returnLoan() {
        if (isReturned()) {
            throw new IllegalStateException("Loan is already returned");
        }
        return copyingBuilder().returned(true).build();
    }

    private Builder copyingBuilder() {
        return Loan.builder()
                .id(getId())
                .memberId(memberId)
                .bookId(bookId)
                .loanDate(loanDate)
                .dueDate(dueDate)
                .returned(returned);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private LoanId id;
        private MemberId memberId;
        private BookId bookId;
        private Instant loanDate;
        private Instant dueDate;
        private boolean returned;

        public Builder id(LoanId id) {
            this.id = id;
            return this;
        }

        public Builder memberId(MemberId member) {
            this.memberId = member;
            return this;
        }

        public Builder bookId(BookId book) {
            this.bookId = book;
            return this;
        }

        public Builder loanDate(Instant loanDate) {
            this.loanDate = loanDate;
            return this;
        }

        public Builder dueDate(Instant dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder returned(boolean returned) {
            this.returned = returned;
            return this;
        }

        public Loan build() {
            return new Loan(this);
        }
    }
}
