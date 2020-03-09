package pl.zmudzin.library.domain.account;

import pl.zmudzin.ddd.annotations.domain.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@ValueObject
@Embeddable
public class Profile implements Serializable {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @SuppressWarnings("unused")
    protected Profile() {
    }

    Profile(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName);
    }

    public void updateFirstName(String firstName) {
        setFirstName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName);
    }

    public void updateLastName(String lastName) {
        setLastName(lastName);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) obj;

        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
