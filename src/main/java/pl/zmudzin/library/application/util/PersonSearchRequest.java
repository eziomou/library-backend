package pl.zmudzin.library.application.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class PersonSearchRequest implements Serializable {

    private String firstName;
    private String lastName;
    private String fullName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PersonSearchRequest)) {
            return false;
        }
        PersonSearchRequest other = (PersonSearchRequest) obj;

        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(fullName, other.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, fullName);
    }
}
