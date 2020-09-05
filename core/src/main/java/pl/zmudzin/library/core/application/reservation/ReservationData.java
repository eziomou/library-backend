package pl.zmudzin.library.core.application.reservation;

import pl.zmudzin.library.core.application.catalog.book.SimpleBookData;
import pl.zmudzin.library.core.application.member.SimpleMemberData;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReservationData {

    private final String id;
    private final SimpleMemberData member;
    private final SimpleBookData book;
    private final List<ReservationEventData> events;

    private ReservationData(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.book = builder.book;
        this.events = builder.events;
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

    public List<ReservationEventData> getEvents() {
        return events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private SimpleMemberData member;
        private SimpleBookData book;
        private List<ReservationEventData> events;

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

        public Builder events(List<ReservationEventData> events) {
            this.events = events;
            return this;
        }

        public <E> Builder events(Collection<E> events, Function<E, ReservationEventData> mapper) {
            this.events = events.stream().map(mapper).collect(Collectors.toList());
            return this;
        }

        public ReservationData build() {
            return new ReservationData(this);
        }
    }
}
