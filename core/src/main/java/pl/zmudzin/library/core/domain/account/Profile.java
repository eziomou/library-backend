package pl.zmudzin.library.core.domain.account;

import java.util.Objects;

public final class Profile {

    private final String firstName;
    private final String lastName;

    public Profile(String firstName, String lastName) {
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Profile))
            return false;
        Profile other = (Profile) object;
        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
