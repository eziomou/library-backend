package pl.zmudzin.library.core.application.account;

public class AccountData {

    private final String username;
    private final ProfileData profile;

    protected AccountData(Builder builder) {
        this.username = builder.username;
        this.profile = new ProfileData(builder.firstName, builder.lastName);
    }

    public String getUsername() {
        return username;
    }

    public ProfileData getProfile() {
        return profile;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String username;
        private String firstName;
        private String lastName;

        protected Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstName(String firstname) {
            this.firstName = firstname;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AccountData build() {
            return new AccountData(this);
        }
    }
}
