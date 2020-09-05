package pl.zmudzin.library.core.application.catalog.book;

public class SimpleBookData {

    private final String id;
    private final String title;

    public SimpleBookData(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private SimpleBookData(Builder<?> builder) {
        this.id = builder.id;
        this.title = builder.title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static <S extends Builder<S>> Builder<S> builder() {
        return new Builder<>();
    }

    public static class Builder<S extends Builder<S>> {

        protected String id;
        protected String title;

        protected Builder() {
        }

        private S self() {
            @SuppressWarnings("unchecked")
            S self = (S) this;
            return self;
        }

        public S id(String id) {
            this.id = id;
            return self();
        }

        public S title(String title) {
            this.title = title;
            return self();
        }

        public SimpleBookData build() {
            return new SimpleBookData(this);
        }
    }
}
