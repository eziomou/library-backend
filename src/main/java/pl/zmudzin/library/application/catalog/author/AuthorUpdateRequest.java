package pl.zmudzin.library.application.catalog.author;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class AuthorUpdateRequest implements Serializable {

    private String firstName;
    private String lastName;

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
}
