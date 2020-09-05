package pl.zmudzin.library.core.application.catalog.publisher;

public final class PublisherData {

    private final String id;
    private final String name;

    public PublisherData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private PublisherData(PublisherData.Builder builder) {
        this(builder.id, builder.name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String name;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public PublisherData build() {
            return new PublisherData(this);
        }
    }
}
