package pl.zmudzin.library.core.application.member;

public class SimpleMemberData {

    private final String id;
    private final String firstName;
    private final String lastName;

    public SimpleMemberData(String id, String memberFirstName, String memberLastName) {
        this.id = id;
        this.firstName = memberFirstName;
        this.lastName = memberLastName;
    }

    private SimpleMemberData(Builder builder) {
        this(builder.id, builder.firstName, builder.lastName);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public SimpleMemberData build() {
            return new SimpleMemberData(this);
        }
    }
}
