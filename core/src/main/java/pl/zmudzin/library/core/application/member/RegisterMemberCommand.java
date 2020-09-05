package pl.zmudzin.library.core.application.member;

public final class RegisterMemberCommand {

    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;

    private RegisterMemberCommand(Builder builder) {
        this.username = builder.username;
        this.password =  builder.password;
        this.firstName =  builder.firstName;
        this.lastName =  builder.lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

        private String username;
        private String password;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastname) {
            this.lastName = lastname;
            return this;
        }

        public RegisterMemberCommand build() {
            return new RegisterMemberCommand(this);
        }
    }
}
