package pl.zmudzin.library.application.catalog.author;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class AuthorBasicData implements Serializable {

    protected Long id;
    protected String firstName;
    protected String lastName;

    AuthorBasicData() {
    }

    public AuthorBasicData(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return firstName + " " + lastName;
    }
}
