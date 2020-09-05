package pl.zmudzin.library.core.application.loan;

import pl.zmudzin.library.core.application.catalog.book.SimpleBookData;
import pl.zmudzin.library.core.application.member.SimpleMemberData;

import java.util.function.Consumer;

public final class LoanData {

    private final String id;
    private final SimpleMemberData member;
    private final SimpleBookData book;
    private final String loanDate;
    private final String dueDate;
    private final boolean returned;

    private LoanData(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.book = builder.book;
        this.loanDate = builder.loanDate;
        this.dueDate = builder.dueDate;
        this.returned = builder.returned;
    }

    public String getId() {
        return id;
    }

    public SimpleMemberData getMember() {
        return member;
    }

    public SimpleBookData getBook() {
        return book;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private SimpleMemberData member;
        private SimpleBookData book;
        private String loanDate;
        private String dueDate;
        private boolean returned;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder member(Consumer<SimpleMemberData.Builder> consumer) {
            SimpleMemberData.Builder builder = SimpleMemberData.builder();
            consumer.accept(builder);
            this.member = builder.build();
            return this;
        }

        public Builder book(Consumer<SimpleBookData.Builder<?>> consumer) {
            SimpleBookData.Builder<?> builder = SimpleBookData.builder();
            consumer.accept(builder);
            this.book = builder.build();
            return this;
        }

        public Builder loanDate(String loanDate) {
            this.loanDate = loanDate;
            return this;
        }

        public Builder dueDate(String dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder returned(boolean returned) {
            this.returned = returned;
            return this;
        }

        public LoanData build() {
            return new LoanData(this);
        }
    }

}
